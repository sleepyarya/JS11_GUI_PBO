package frontend;

import backend.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// FrmKategori mewarisi (extends) dari JFrame
public class FrmKategori extends JFrame {
    
    // Deklarasi Komponen GUI
    private JTextField txtIdKategori;
    private JTextField txtNama;
    private JTextField txtKeterangan;
    private JTextField txtCari;
    
    private JButton btnSimpan;
    private JButton btnTambahBaru;
    private JButton btnHapus;
    private JButton btnCari;
    
    private JTable tblKategori;
    private JScrollPane scrollPane;

    public FrmKategori() {
        // Panggil method untuk inisialisasi komponen
        initComponents();
        // Tampilkan data kategori saat form pertama kali dibuka
        tampilkanData();
        // Kosongkan form saat pertama kali dibuka
        kosongkanForm();
    }

    // Metode untuk inisialisasi komponen
    private void initComponents() {
        // Konfigurasi Frame Utama
        setTitle("Manajemen Data Kategori");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Layout utama: BorderLayout
        
        // --- Panel Input (North/Atas) ---
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 5, 5));
        
        txtIdKategori = new JTextField();
        txtIdKategori.setEnabled(false); // ID tidak bisa diedit
        txtNama = new JTextField();
        txtKeterangan = new JTextField();

        panelInput.add(new JLabel("ID Kategori:"));
        panelInput.add(txtIdKategori);
        panelInput.add(new JLabel("Nama:"));
        panelInput.add(txtNama);
        panelInput.add(new JLabel("Keterangan:"));
        panelInput.add(txtKeterangan);
        
        // Tambahkan panel input ke bagian atas Frame
        add(panelInput, BorderLayout.NORTH);

        // --- Panel Tombol (Center) ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnSimpan = new JButton("Simpan");
        btnTambahBaru = new JButton("Tambah Baru");
        btnHapus = new JButton("Hapus");
        txtCari = new JTextField(15);
        btnCari = new JButton("Cari");
        
        panelTombol.add(btnSimpan);
        panelTombol.add(btnTambahBaru);
        panelTombol.add(btnHapus);
        panelTombol.add(txtCari);
        panelTombol.add(btnCari);
        
        // --- Panel Tabel (South/Bawah) ---
        tblKategori = new JTable();
        scrollPane = new JScrollPane(tblKategori);
        
        // Kita akan menggunakan BorderLayout.CENTER untuk menempatkan tombol dan tabel
        // Mari kita buat satu panel utama untuk Tombol & Tabel
        JPanel panelKonten = new JPanel(new BorderLayout());
        panelKonten.add(panelTombol, BorderLayout.NORTH);
        panelKonten.add(scrollPane, BorderLayout.CENTER);
        
        add(panelKonten, BorderLayout.CENTER);
        
        // --- Register Event Listeners ---
        
        // 1. Simpan
        btnSimpan.addActionListener(e -> {
            Kategori kat = new Kategori();
            
            // Cek apakah ID sudah ada (Update) atau belum (Insert)
            try {
                kat.setIdkategori(Integer.parseInt(txtIdKategori.getText()));
            } catch (NumberFormatException ex) {
                // Jika kosong, biarkan ID 0 (untuk insert)
            }
            
            kat.setNama(txtNama.getText());
            kat.setKeterangan(txtKeterangan.getText());
            kat.save();
            
            // Set ID baru jika berhasil insert
            txtIdKategori.setText(String.valueOf(kat.getIdkategori()));
            tampilkanData(); // Refresh tabel
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        });

        // 2. Tambah Baru
        btnTambahBaru.addActionListener(e -> {
            kosongkanForm();
        });

        // 3. Hapus
        btnHapus.addActionListener(e -> {
            if (txtIdKategori.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, 
                                "Yakin ingin menghapus data ID " + txtIdKategori.getText() + "?", 
                                "Konfirmasi Hapus", 
                                JOptionPane.YES_NO_OPTION);
            
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Kategori kat = new Kategori().getById(Integer.parseInt(txtIdKategori.getText()));
                kat.delete();
                kosongkanForm();
                tampilkanData();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            }
        });

        // 4. Cari
        btnCari.addActionListener(e -> {
            cari(txtCari.getText());
        });

        // 5. Klik Tabel (untuk memilih data)
        tblKategori.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblKategori.getSelectedRow() != -1) {
                    // Ambil baris yang dipilih
                    int row = tblKategori.getSelectedRow();
                    // ID ada di kolom ke-0
                    int id = (int) tblKategori.getModel().getValueAt(row, 0);
                    
                    // Ambil objek Kategori berdasarkan ID
                    Kategori kat = new Kategori().getById(id);
                    
                    // Tampilkan data ke dalam form
                    txtIdKategori.setText(String.valueOf(kat.getIdkategori()));
                    txtNama.setText(kat.getNama());
                    txtKeterangan.setText(kat.getKeterangan());
                }
            }
        });

        // Frame harus di-set visible di akhir
        setVisible(true);
    }
    
    // Metode untuk mengosongkan form
    public void kosongkanForm() {
        txtIdKategori.setText("0"); // Set ID menjadi 0 untuk operasi Insert
        txtNama.setText("");
        txtKeterangan.setText("");
    }

    // Metode untuk menampilkan data ke dalam tabel
    public final void tampilkanData() {
        String[] kolom = {"ID", "Nama", "Keterangan"};
        ArrayList<Kategori> list = new Kategori().getAll();
        Object rowData[] = new Object[3];
        
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        
        for (Kategori kat : list) {
            rowData[0] = kat.getIdkategori();
            rowData[1] = kat.getNama();
            rowData[2] = kat.getKeterangan();
            
            model.addRow(rowData);
        }
        
        tblKategori.setModel(model);
    }

    // Metode untuk mencari data dan menampilkan hasilnya ke tabel
    public final void cari(String keyword) {
        String[] kolom = {"ID", "Nama", "Keterangan"};
        ArrayList<Kategori> list = new Kategori().search(keyword);
        Object rowData[] = new Object[3];
        
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        
        for (Kategori kat : list) {
            rowData[0] = kat.getIdkategori();
            rowData[1] = kat.getNama();
            rowData[2] = kat.getKeterangan();
            
            model.addRow(rowData);
        }
        
        tblKategori.setModel(model);
    }
    
    // Main method untuk menjalankan form (Opsional, bisa dipisah di TestBackend)
    // public static void main(String[] args) {
    //     new FrmKategori();
    // }
}