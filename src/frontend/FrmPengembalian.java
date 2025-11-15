package frontend;

import backend.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class FrmPengembalian extends JFrame {

    private JTextField txtIdPeminjaman;
    private JButton btnCari;
    private JButton btnProsesKembali;

    private JLabel lblIdPinjam;
    private JLabel lblNamaAnggota;
    private JLabel lblJudulBuku;
    private JLabel lblTglPinjam;
    private JLabel lblTglKembali;
    private JLabel lblDenda;
    
    private Peminjaman peminjamanSaatIni; 

    public FrmPengembalian() {
        initComponents();
        kosongkanTampilan();
    }

    private void initComponents() {
        setTitle("Proses Pengembalian Buku");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10));

        // --- Panel Input dan Cari (NORTH) ---
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtIdPeminjaman = new JTextField(10);
        btnCari = new JButton("Cari Transaksi");
        
        panelCari.add(new JLabel("ID Peminjaman:"));
        panelCari.add(txtIdPeminjaman);
        panelCari.add(btnCari);
        
        add(panelCari, BorderLayout.NORTH);

        // --- Panel Detail Transaksi (CENTER) ---
        JPanel panelDetail = new JPanel(new GridLayout(6, 2, 5, 5));
        
        lblIdPinjam = new JLabel("<<ID>>");
        lblNamaAnggota = new JLabel("<<Nama Anggota>>");
        lblJudulBuku = new JLabel("<<Judul Buku>>");
        lblTglPinjam = new JLabel("<<Tanggal Pinjam>>");
        lblTglKembali = new JLabel("<<Tanggal Harus Kembali>>");
        lblDenda = new JLabel("Rp 0");
        lblDenda.setForeground(Color.RED); 
        
        panelDetail.setBorder(BorderFactory.createTitledBorder("Detail Transaksi"));

        panelDetail.add(new JLabel("ID Transaksi:"));
        panelDetail.add(lblIdPinjam);
        panelDetail.add(new JLabel("Anggota:"));
        panelDetail.add(lblNamaAnggota);
        panelDetail.add(new JLabel("Buku:"));
        panelDetail.add(lblJudulBuku);
        panelDetail.add(new JLabel("Tgl Pinjam:"));
        panelDetail.add(lblTglPinjam);
        panelDetail.add(new JLabel("Tgl Jatuh Tempo:"));
        panelDetail.add(lblTglKembali);
        panelDetail.add(new JLabel("TOTAL DENDA:"));
        panelDetail.add(lblDenda);

        add(panelDetail, BorderLayout.CENTER);

        // --- Panel Aksi (SOUTH) ---
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnProsesKembali = new JButton("Proses Pengembalian");
        btnProsesKembali.setEnabled(false);
        panelAksi.add(btnProsesKembali);
        
        add(panelAksi, BorderLayout.SOUTH);

        addEventListeners();
        setVisible(true);
    }
    
    private void kosongkanTampilan() {
        peminjamanSaatIni = null;
        txtIdPeminjaman.setText("");
        lblIdPinjam.setText("<<ID>>");
        lblNamaAnggota.setText("<<Nama Anggota>>");
        lblJudulBuku.setText("<<Judul Buku>>");
        lblTglPinjam.setText("<<Tanggal Pinjam>>");
        lblTglKembali.setText("<<Tanggal Harus Kembali>>");
        lblDenda.setText("Rp 0");
        btnProsesKembali.setEnabled(false);
    }

    private void addEventListeners() {
        
        btnCari.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdPeminjaman.getText());
                peminjamanSaatIni = new Peminjaman().getById(id);

                if (peminjamanSaatIni.getIdpeminjaman() != 0) {
                    
                    long hariTerlambat = hitungKeterlambatan(peminjamanSaatIni.getTanggalKembali());
                    long denda = hariTerlambat * 1000; 
                    
                    lblIdPinjam.setText(String.valueOf(peminjamanSaatIni.getIdpeminjaman()));
                    lblNamaAnggota.setText(peminjamanSaatIni.getAnggota().getNama());
                    lblJudulBuku.setText(peminjamanSaatIni.getBuku().getJudul());
                    lblTglPinjam.setText(peminjamanSaatIni.getTanggalPinjam());
                    lblTglKembali.setText(peminjamanSaatIni.getTanggalKembali());
                    lblDenda.setText("Rp " + denda + " (Terlambat " + hariTerlambat + " hari)");
                    
                    btnProsesKembali.setEnabled(true);

                } else {
                    JOptionPane.showMessageDialog(this, "ID Peminjaman tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                    kosongkanTampilan();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan ID Peminjaman yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                kosongkanTampilan();
            }
        });
        
        btnProsesKembali.addActionListener(e -> {
            if (peminjamanSaatIni != null && peminjamanSaatIni.getIdpeminjaman() != 0) {
                
                long hariTerlambat = hitungKeterlambatan(peminjamanSaatIni.getTanggalKembali());
                long denda = hariTerlambat * 1000;
                
                String pesanDenda = (denda > 0) ? " Total denda: Rp " + denda : "";
                
                int konfirmasi = JOptionPane.showConfirmDialog(this, 
                        "Proses pengembalian untuk ID " + peminjamanSaatIni.getIdpeminjaman() + "." + pesanDenda, 
                        "Konfirmasi Pengembalian", 
                        JOptionPane.YES_NO_OPTION);
                
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    // Logika Pengembalian: Menghapus data dari tabel Peminjaman
                    peminjamanSaatIni.delete(); 
                    JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan." + pesanDenda, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    kosongkanTampilan();
                }
            }
        });
    }
    
    private long hitungKeterlambatan(String tglJatuhTempo) {
        LocalDate tglKembali = LocalDate.now();
        LocalDate tglJatuhTempoDate = LocalDate.parse(tglJatuhTempo);
        
        if (tglKembali.isAfter(tglJatuhTempoDate)) {
            return ChronoUnit.DAYS.between(tglJatuhTempoDate, tglKembali);
        }
        return 0; 
    }
}