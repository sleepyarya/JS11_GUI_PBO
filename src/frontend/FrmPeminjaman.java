package frontend;

import backend.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FrmPeminjaman extends JFrame {

    private JTextField txtIdPeminjaman;
    private JTextField txtIdAnggota;
    private JTextField txtIdBuku;
    private JTextField txtTglPinjam;
    private JTextField txtTglKembali;
    private JTextField txtCari;

    private JButton btnSimpan;
    private JButton btnTambahBaru;
    private JButton btnHapus;
    private JButton btnCariAnggota;
    private JButton btnCariBuku;
    private JButton btnCariTabel;

    private JLabel lblNamaAnggota;
    private JLabel lblJudulBuku;
    private JTable tblPeminjaman;
    private JScrollPane scrollPane;

    public FrmPeminjaman() {
        initComponents();
        kosongkanForm();
        tampilkanData();
    }

    private void initComponents() {
        setTitle("Transaksi Peminjaman");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Input (NORTH) ---
        // Layout 7 baris, 3 kolom untuk kerapihan
        JPanel panelInput = new JPanel(new GridLayout(7, 3, 5, 5)); 
        
        txtIdPeminjaman = new JTextField();
        txtIdPeminjaman.setEnabled(false);
        txtIdAnggota = new JTextField();
        txtIdBuku = new JTextField();
        txtTglPinjam = new JTextField();
        txtTglKembali = new JTextField();

        lblNamaAnggota = new JLabel("<< Belum Dipilih >>"); 
        lblJudulBuku = new JLabel("<< Belum Dipilih >>");    

        btnCariAnggota = new JButton("Cari Anggota");
        btnCariBuku = new JButton("Cari Buku");
        
        // Baris 1: ID Peminjaman
        panelInput.add(new JLabel("ID Peminjaman:"));
        panelInput.add(txtIdPeminjaman);
        panelInput.add(new JLabel("")); 

        // Baris 2: ID Anggota + Tombol Cari
        panelInput.add(new JLabel("ID Anggota:"));
        panelInput.add(txtIdAnggota);
        panelInput.add(btnCariAnggota);

        // Baris 3: Nama Anggota (Output)
        panelInput.add(new JLabel("Nama Anggota:"));
        panelInput.add(lblNamaAnggota); 
        panelInput.add(new JLabel("")); 

        // Baris 4: ID Buku + Tombol Cari
        panelInput.add(new JLabel("ID Buku:"));
        panelInput.add(txtIdBuku);
        panelInput.add(btnCariBuku);

        // Baris 5: Judul Buku (Output)
        panelInput.add(new JLabel("Judul Buku:"));
        panelInput.add(lblJudulBuku); 
        panelInput.add(new JLabel("")); 
        
        // Baris 6: Tgl Pinjam
        panelInput.add(new JLabel("Tgl Pinjam (YYYY-MM-DD):"));
        panelInput.add(txtTglPinjam);
        panelInput.add(new JLabel("")); 
        
        // Baris 7: Tgl Kembali
        panelInput.add(new JLabel("Tgl Kembali (YYYY-MM-DD):"));
        panelInput.add(txtTglKembali);
        panelInput.add(new JLabel("")); 

        add(panelInput, BorderLayout.NORTH);

        // --- Panel Tombol & Tabel (CENTER) ---
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnSimpan = new JButton("Simpan");
        btnTambahBaru = new JButton("Tambah Baru");
        btnHapus = new JButton("Hapus");
        btnCariTabel = new JButton("Cari");
        txtCari = new JTextField(20);

        panelAksi.add(btnSimpan);
        panelAksi.add(btnTambahBaru);
        panelAksi.add(btnHapus);
        panelAksi.add(txtCari);
        panelAksi.add(btnCariTabel);

        tblPeminjaman = new JTable();
        scrollPane = new JScrollPane(tblPeminjaman);

        JPanel panelKonten = new JPanel(new BorderLayout());
        panelKonten.add(panelAksi, BorderLayout.NORTH);
        panelKonten.add(scrollPane, BorderLayout.CENTER);

        add(panelKonten, BorderLayout.CENTER);

        // --- Event Listeners ---
        addEventListeners();

        setVisible(true);
    }
    
    private void addEventListeners() {
        
        btnSimpan.addActionListener(e -> {
            Peminjaman p = new Peminjaman();
            try {
                p.setIdpeminjaman(Integer.parseInt(txtIdPeminjaman.getText()));
            } catch (NumberFormatException ex) {}
            
            Anggota a = new Anggota().getById(Integer.parseInt(txtIdAnggota.getText()));
            Buku b = new Buku().getById(Integer.parseInt(txtIdBuku.getText()));
            
            if (a.getIdanggota() == 0 || b.getIdbuku() == 0) {
                 JOptionPane.showMessageDialog(this, "ID Anggota atau ID Buku tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            p.setAnggota(a);
            p.setBuku(b);
            p.setTanggalPinjam(txtTglPinjam.getText());
            p.setTanggalKembali(txtTglKembali.getText());
            p.save();
            
            txtIdPeminjaman.setText(String.valueOf(p.getIdpeminjaman()));
            tampilkanData();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
        });

        btnTambahBaru.addActionListener(e -> {
            kosongkanForm();
        });

        btnHapus.addActionListener(e -> {
            if (txtIdPeminjaman.getText().isEmpty() || txtIdPeminjaman.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Pilih data peminjaman dari tabel untuk dihapus!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, 
                                "Yakin ingin menghapus transaksi ID " + txtIdPeminjaman.getText() + "?", 
                                "Konfirmasi Hapus", 
                                JOptionPane.YES_NO_OPTION);
            
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Peminjaman p = new Peminjaman().getById(Integer.parseInt(txtIdPeminjaman.getText()));
                p.delete();
                kosongkanForm();
                tampilkanData();
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            }
        });

        btnCariAnggota.addActionListener(e -> {
            try {
                int idAnggota = Integer.parseInt(txtIdAnggota.getText());
                Anggota a = new Anggota().getById(idAnggota);
                if (a.getIdanggota() != 0) {
                    lblNamaAnggota.setText(a.getNama());
                } else {
                    lblNamaAnggota.setText("ID Anggota tidak ditemukan.");
                }
            } catch (NumberFormatException ex) {
                lblNamaAnggota.setText("Input ID Anggota salah.");
            }
        });

        btnCariBuku.addActionListener(e -> {
            try {
                int idBuku = Integer.parseInt(txtIdBuku.getText());
                Buku b = new Buku().getById(idBuku);
                if (b.getIdbuku() != 0) {
                    lblJudulBuku.setText(b.getJudul());
                } else {
                    lblJudulBuku.setText("ID Buku tidak ditemukan.");
                }
            } catch (NumberFormatException ex) {
                lblJudulBuku.setText("Input ID Buku salah.");
            }
        });

        btnCariTabel.addActionListener(e -> {
            cari(txtCari.getText());
        });

        tblPeminjaman.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblPeminjaman.getSelectedRow() != -1) {
                    int row = tblPeminjaman.getSelectedRow();
                    int id = (int) tblPeminjaman.getModel().getValueAt(row, 0); 
                    
                    Peminjaman p = new Peminjaman().getById(id);
                    
                    txtIdPeminjaman.setText(String.valueOf(p.getIdpeminjaman()));
                    txtIdAnggota.setText(String.valueOf(p.getAnggota().getIdanggota()));
                    lblNamaAnggota.setText(p.getAnggota().getNama());
                    txtIdBuku.setText(String.valueOf(p.getBuku().getIdbuku()));
                    lblJudulBuku.setText(p.getBuku().getJudul());
                    txtTglPinjam.setText(p.getTanggalPinjam());
                    txtTglKembali.setText(p.getTanggalKembali());
                }
            }
        });
    }

    public void kosongkanForm() {
        Peminjaman defaultPinjam = new Peminjaman(); 
        
        txtIdPeminjaman.setText("0");
        txtIdAnggota.setText("");
        txtIdBuku.setText("");
        txtTglPinjam.setText(defaultPinjam.getTanggalPinjam());
        txtTglKembali.setText(defaultPinjam.getTanggalKembali());
        lblNamaAnggota.setText("");
        lblJudulBuku.setText("");
        txtCari.setText("");
    }

    public final void tampilkanData() {
        String[] kolom = {"ID Pinjam", "Nama Anggota", "Judul Buku", "Tgl Pinjam", "Tgl Kembali"};
        ArrayList<Peminjaman> list = new Peminjaman().getAll();
        Object rowData[] = new Object[5];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Peminjaman p : list) {
            rowData[0] = p.getIdpeminjaman();
            rowData[1] = p.getAnggota().getNama();
            rowData[2] = p.getBuku().getJudul();
            rowData[3] = p.getTanggalPinjam();
            rowData[4] = p.getTanggalKembali();

            model.addRow(rowData);
        }

        tblPeminjaman.setModel(model);
    }

    public final void cari(String keyword) {
        String[] kolom = {"ID Pinjam", "Nama Anggota", "Judul Buku", "Tgl Pinjam", "Tgl Kembali"};
        ArrayList<Peminjaman> list = new Peminjaman().search(keyword);
        Object rowData[] = new Object[5];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Peminjaman p : list) {
            rowData[0] = p.getIdpeminjaman();
            rowData[1] = p.getAnggota().getNama();
            rowData[2] = p.getBuku().getJudul();
            rowData[3] = p.getTanggalPinjam();
            rowData[4] = p.getTanggalKembali();

            model.addRow(rowData);
        }

        tblPeminjaman.setModel(model);
    }
}