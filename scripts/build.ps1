param(
    [string]$JdkPath = ''
)

# If a JDK path was provided, set JAVA_HOME for this session
if ($JdkPath -ne '') {
    if (Test-Path $JdkPath) {
        $env:JAVA_HOME = $JdkPath
        $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    } else {
        Write-Error "Provided JdkPath '$JdkPath' does not exist. Aborting."
        exit 2
    }
}

Write-Host "Using java: " (Get-Command java).Source
Write-Host "Java version:"
java -version

# Create bin directory
$binDir = Join-Path -Path (Get-Location) -ChildPath 'bin'
if (-not (Test-Path $binDir)) { New-Item -ItemType Directory -Path $binDir | Out-Null }

# Collect all .java files under src
$sources = Get-ChildItem -Path (Join-Path (Get-Location) 'src') -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if ($sources.Count -eq 0) {
    Write-Error "No Java source files found under 'src'"
    exit 1
}

# Compile using --release 21
Write-Host "Compiling $($sources.Count) java files to $binDir with --release 21..."
& javac --release 21 -d $binDir @($sources) 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed. See output above."
    exit $LASTEXITCODE
}

Write-Host "Compilation succeeded. Classes in: $binDir"

# Helpful hint: how to run a main class (example)
Write-Host "To run a main class: java -cp bin FullyQualifiedMainClassName"}