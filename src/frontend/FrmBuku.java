package frontend;

import backend.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FrmBuku extends JFrame {

    // Deklarasi Komponen
    private JTextField txtIdBuku;
    private JComboBox cmbKategori; // GANTI JTextField menjadi JComboBox
    private JTextField txtJudul;
    private JTextField txtPenerbit;
    private JTextField txtPenulis;
    private JTextField txtCari;

    private JButton btnSimpan;
    private JButton btnTambahBaru;
    private JButton btnHapus;
    private JButton btnCari;

    private JTable tblBuku;
    private JScrollPane scrollPane;

    public FrmBuku() {
        initComponents();
        tampilkanCmbKategori(); 
        kosongkanForm();
        // Memuat data kategori ke ComboBox saat form dibuka
        tampilkanData();
    }

    private void initComponents() {
        setTitle("Manajemen Data Buku");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Gunakan DISPOSE_ON_CLOSE agar aplikasi tidak keluar saat form ini ditutup
        setLayout(new BorderLayout(10, 10));

        // --- Panel Input (NORTH) ---
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 5, 5));

        txtIdBuku = new JTextField();
        txtIdBuku.setEnabled(false);
        cmbKategori = new JComboBox(); // Inisialisasi ComboBox
        txtJudul = new JTextField();
        txtPenerbit = new JTextField();
        txtPenulis = new JTextField();
        txtCari = new JTextField(15);

        panelInput.add(new JLabel("ID Buku:"));
        panelInput.add(txtIdBuku);
        panelInput.add(new JLabel("Kategori:"));
        panelInput.add(cmbKategori); // Tambahkan ComboBox
        panelInput.add(new JLabel("Judul:"));
        panelInput.add(txtJudul);
        panelInput.add(new JLabel("Penerbit:"));
        panelInput.add(txtPenerbit);
        panelInput.add(new JLabel("Penulis:"));
        panelInput.add(txtPenulis);

        add(panelInput, BorderLayout.NORTH);

        // --- Panel Tombol & Tabel (CENTER) ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnSimpan = new JButton("Simpan");
        btnTambahBaru = new JButton("Tambah Baru");
        btnHapus = new JButton("Hapus");
        btnCari = new JButton("Cari");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnTambahBaru);
        panelTombol.add(btnHapus);
        panelTombol.add(txtCari);
        panelTombol.add(btnCari);

        tblBuku = new JTable();
        scrollPane = new JScrollPane(tblBuku);

        JPanel panelKonten = new JPanel(new BorderLayout());
        panelKonten.add(panelTombol, BorderLayout.NORTH);
        panelKonten.add(scrollPane, BorderLayout.CENTER);

        add(panelKonten, BorderLayout.CENTER);

        // --- Event Listeners ---
        
        // 1. Simpan
        btnSimpan.addActionListener(e -> {
            Buku buku = new Buku();
            Kategori kategoriDipilih = (Kategori) cmbKategori.getSelectedItem();
            
            try {
                buku.setIdbuku(Integer.parseInt(txtIdBuku.getText()));
            } catch (NumberFormatException ex) {
                // Biarkan ID 0 untuk insert
            }
            
            buku.setKategori(kategoriDipilih);
            buku.setJudul(txtJudul.getText());
            buku.setPenerbit(txtPenerbit.getText());
            buku.setPenulis(txtPenulis.getText());
            buku.save();
            
            txtIdBuku.setText(String.valueOf(buku.getIdbuku()));
            tampilkanData();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        });

        // 2. Tambah Baru
        btnTambahBaru.addActionListener(e -> {
            kosongkanForm();
        });

        // 3. Hapus
        btnHapus.addActionListener(e -> {
            if (txtIdBuku.getText().isEmpty() || txtIdBuku.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, 
                                "Yakin ingin menghapus data ID " + txtIdBuku.getText() + "?", 
                                "Konfirmasi Hapus", 
                                JOptionPane.YES_NO_OPTION);
            
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Buku buku = new Buku().getById(Integer.parseInt(txtIdBuku.getText()));
                buku.delete();
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
        tblBuku.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblBuku.getSelectedRow() != -1) {
                    int row = tblBuku.getSelectedRow();
                    // ID ada di kolom ke-0 (ID Buku)
                    int id = (int) tblBuku.getModel().getValueAt(row, 0); 
                    
                    Buku buku = new Buku().getById(id);
                    
                    txtIdBuku.setText(String.valueOf(buku.getIdbuku()));
                    
                    // Memilih item di ComboBox berdasarkan objek Kategori
                    cmbKategori.setSelectedItem(buku.getKategori());
                    
                    txtJudul.setText(buku.getJudul());
                    txtPenerbit.setText(buku.getPenerbit());
                    txtPenulis.setText(buku.getPenulis());
                }
            }
        });

        setVisible(true);
    }

    // Metode untuk mengosongkan form
    public void kosongkanForm() {
        txtIdBuku.setText("0");
        cmbKategori.setSelectedIndex(0); // Pilih item pertama di ComboBox
        txtJudul.setText("");
        txtPenerbit.setText("");
        txtPenulis.setText("");
    }
    
    // Metode PENTING: Mengisi JComboBox dengan objek Kategori
    public final void tampilkanCmbKategori() {
        // Hapus item lama (jika ada)
        cmbKategori.removeAllItems(); 
        
        // Ambil semua Kategori dari database
        for (Kategori kat : new Kategori().getAll()) {
            cmbKategori.addItem(kat);
        }
    }

    // Metode untuk menampilkan data Buku ke dalam tabel
    public final void tampilkanData() {
        String[] kolom = {"ID", "Kategori", "Judul", "Penerbit", "Penulis"};
        ArrayList<Buku> list = new Buku().getAll();
        Object rowData[] = new Object[5];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Buku buku : list) {
            rowData[0] = buku.getIdbuku();
            rowData[1] = buku.getKategori().getNama(); // Tampilkan Nama Kategori
            rowData[2] = buku.getJudul();
            rowData[3] = buku.getPenerbit();
            rowData[4] = buku.getPenulis();

            model.addRow(rowData);
        }

        tblBuku.setModel(model);
    }

    // Metode untuk mencari data dan menampilkan hasilnya ke tabel
    public final void cari(String keyword) {
        String[] kolom = {"ID", "Kategori", "Judul", "Penerbit", "Penulis"};
        ArrayList<Buku> list = new Buku().search(keyword);
        Object rowData[] = new Object[5];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Buku buku : list) {
            rowData[0] = buku.getIdbuku();
            rowData[1] = buku.getKategori().getNama(); // Tampilkan Nama Kategori
            rowData[2] = buku.getJudul();
            rowData[3] = buku.getPenerbit();
            rowData[4] = buku.getPenulis();

            model.addRow(rowData);
        }

        tblBuku.setModel(model);
    }
}