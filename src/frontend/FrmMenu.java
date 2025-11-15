package frontend;

import javax.swing.*;

public class FrmMenu extends JFrame {
    
    // Deklarasi Komponen Menu
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuDataMaster;
    private JMenu menuTransaksi;
    private JMenu menuLaporan;

    private JMenuItem menuItemKeluar;
    private JMenuItem menuItemKategori;
    private JMenuItem menuItemBuku;
    private JMenuItem menuItemAnggota;
    private JMenuItem menuItemPeminjaman;
    private JMenuItem menuItemPengembalian;
    private JMenuItem menuItemLaporan; 

    public FrmMenu() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Informasi Perpustakaan - Menu Utama");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Menempatkan form di tengah layar
        
        // Inisialisasi Menu Bar
        menuBar = new JMenuBar();
        
        // Inisialisasi Menu
        menuFile = new JMenu("File");
        menuDataMaster = new JMenu("Data Master");
        menuTransaksi = new JMenu("Transaksi");
        menuLaporan = new JMenu("Laporan");
        
        // Inisialisasi Menu Item
        menuItemKeluar = new JMenuItem("Keluar");
        menuItemKategori = new JMenuItem("Data Kategori");
        menuItemBuku = new JMenuItem("Data Buku");
        menuItemAnggota = new JMenuItem("Data Anggota");
        menuItemPeminjaman = new JMenuItem("Peminjaman");
        menuItemPengembalian = new JMenuItem("Pengembalian");
        menuItemLaporan = new JMenuItem("Laporan Peminjaman");

        // --- Susun Menu ---
        
        // Menu File
        menuFile.add(menuItemKeluar);
        
        // Menu Data Master
        menuDataMaster.add(menuItemKategori);
        menuDataMaster.add(menuItemBuku);
        menuDataMaster.add(menuItemAnggota);
        
        // Menu Transaksi
        menuTransaksi.add(menuItemPeminjaman);
        menuTransaksi.add(menuItemPengembalian);
        
        // Menu Laporan
        menuLaporan.add(menuItemLaporan);
        
        // --- Masukkan ke Menu Bar ---
        menuBar.add(menuFile);
        menuBar.add(menuDataMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);
        
        // Set Menu Bar ke Frame
        setJMenuBar(menuBar);

        // --- Event Listeners ---
        addEventListeners();
        
        // Tampilkan frame
        setVisible(true);
    }
    
    private void addEventListeners() {
        // 1. Keluar Aplikasi
        menuItemKeluar.addActionListener(e -> {
            System.exit(0);
        });

        // 2. Data Kategori
        menuItemKategori.addActionListener(e -> {
            new FrmKategori();
        });
        
        // 3. Data Buku
        menuItemBuku.addActionListener(e -> {
            new FrmBuku();
        });
        
        // 4. Data Anggota
        menuItemAnggota.addActionListener(e -> {
            new FrmAnggota();
        });
        
        // 5. Transaksi Peminjaman
        menuItemPeminjaman.addActionListener(e -> {
            new FrmPeminjaman();
        });
        
        // 6. Transaksi Pengembalian (Sudah Fix)
        menuItemPengembalian.addActionListener(e -> {
            new FrmPengembalian(); 
        });
        
        // 7. Laporan Peminjaman (Sudah Fix)
        menuItemLaporan.addActionListener(e -> {
            new FrmLaporanPeminjaman(); 
        });
    }

    public static void main(String[] args) {
        // Pastikan kelas ini yang dijalankan sebagai main class aplikasi Anda
        SwingUtilities.invokeLater(() -> {
            new FrmMenu();
        });
    }
}