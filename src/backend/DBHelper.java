package backend;
import java.sql.*;

public class DBHelper {
    private static Connection koneksi;
    
    public static void bukakoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/perpustakaan"; 
                String user = "root";    
                String password = "";    
                
                // Gunakan Class.forName untuk driver modern
                Class.forName("com.mysql.cj.jdbc.Driver"); 
                
                koneksi = DriverManager.getConnection(url, user, password);
                
                // --- LOGGING SUKSES KONEKSI ---
                System.out.println("✅ KONEKSI DATABASE BERHASIL!"); 
                // -----------------------------
            } catch (ClassNotFoundException e) {
                System.err.println("❌ DRIVER JDBC TIDAK DITEMUKAN. Cek file JAR di Classpath.");
                e.printStackTrace();
            } catch (SQLException t) {
                // --- LOGGING GAGAL KONEKSI ---
                System.err.println("❌ ERROR KONEKSI DATABASE! Cek status MySQL Server, URL, atau Port.");
                t.printStackTrace();
                // -----------------------------
            }
        }
    }
    
    public static int insertQueryGetId(String query) {
        bukakoneksi();
        int num = 0;
        int result = -1; 
        try {
            // --- LOGGING QUERY ---
            System.out.println("➡️ Executing INSERT: " + query); 
            // ---------------------
            
            Statement stmt = koneksi.createStatement(); 
            num = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS); 
            
            ResultSet rs = stmt.getGeneratedKeys(); 
            if (rs.next()) { 
                result = rs.getInt(1); 
            }
            rs.close(); 
            stmt.close(); 
            System.out.println("✅ INSERT Berhasil! ID baru: " + result);
        } catch (Exception e) {
            // --- LOGGING GAGAL EKSEKUSI ---
            System.err.println("❌ ERROR EKSEKUSI QUERY INSERT!");
            // ------------------------------
            e.printStackTrace(); 
            result = -1; 
        }
        return result; 
    }
    
    public static boolean executeQuery(String query) {
        bukakoneksi(); 
        boolean result = false; 
        try {
            // --- LOGGING QUERY ---
            System.out.println("➡️ Executing UPDATE/DELETE: " + query); 
            // ---------------------
            
            Statement stmt = koneksi.createStatement(); 
            stmt.executeUpdate(query); 
            result = true; 
            stmt.close(); 
            System.out.println("✅ UPDATE/DELETE Berhasil!");
        } catch (Exception e) {
            // --- LOGGING GAGAL EKSEKUSI ---
            System.err.println("❌ ERROR EKSEKUSI QUERY UPDATE/DELETE!"); 
            // ------------------------------
            e.printStackTrace(); 
        }
        return result; 
    }
    
    public static ResultSet selectQuery(String query) {
        bukakoneksi(); 
        ResultSet rs = null; 
        try {
            Statement stmt = koneksi.createStatement(); 
            rs = stmt.executeQuery(query); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return rs; 
    }
}