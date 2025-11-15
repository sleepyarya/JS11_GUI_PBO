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

public class FrmAnggota extends JFrame {

    private JTextField txtIdAnggota;
    private JTextField txtNama;
    private JTextField txtAlamat;
    private JTextField txtTelepon;
    private JTextField txtCari;

    private JButton btnSimpan;
    private JButton btnTambahBaru;
    private JButton btnHapus;
    private JButton btnCari;

    private JTable tblAnggota;
    private JScrollPane scrollPane;

    public FrmAnggota() {
        initComponents();
        tampilkanData();
        kosongkanForm();
    }

    private void initComponents() {
        setTitle("Manajemen Data Anggota");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Input (NORTH) ---
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));

        txtIdAnggota = new JTextField();
        txtIdAnggota.setEnabled(false);
        txtNama = new JTextField();
        txtAlamat = new JTextField();
        txtTelepon = new JTextField();

        panelInput.add(new JLabel("ID Anggota:"));
        panelInput.add(txtIdAnggota);
        panelInput.add(new JLabel("Nama:"));
        panelInput.add(txtNama);
        panelInput.add(new JLabel("Alamat:"));
        panelInput.add(txtAlamat);
        panelInput.add(new JLabel("Telepon:"));
        panelInput.add(txtTelepon);

        add(panelInput, BorderLayout.NORTH);

        // --- Panel Tombol & Tabel (CENTER) ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnSimpan = new JButton("Simpan");
        btnTambahBaru = new JButton("Tambah Baru");
        btnHapus = new JButton("Hapus");
        btnCari = new JButton("Cari");
        txtCari = new JTextField(15);

        panelTombol.add(btnSimpan);
        panelTombol.add(btnTambahBaru);
        panelTombol.add(btnHapus);
        panelTombol.add(txtCari);
        panelTombol.add(btnCari);

        tblAnggota = new JTable();
        scrollPane = new JScrollPane(tblAnggota);

        JPanel panelKonten = new JPanel(new BorderLayout());
        panelKonten.add(panelTombol, BorderLayout.NORTH);
        panelKonten.add(scrollPane, BorderLayout.CENTER);

        add(panelKonten, BorderLayout.CENTER);

        // --- Event Listeners ---
        
        btnSimpan.addActionListener(e -> {
            Anggota a = new Anggota();
            
            try {
                a.setIdanggota(Integer.parseInt(txtIdAnggota.getText()));
            } catch (NumberFormatException ex) {
            }
            
            a.setNama(txtNama.getText());
            a.setAlamat(txtAlamat.getText());
            a.setTelepon(txtTelepon.getText());
            a.save();
            
            txtIdAnggota.setText(String.valueOf(a.getIdanggota()));
            tampilkanData();
            JOptionPane.showMessageDialog(this, "Data Anggota berhasil disimpan!");
        });

        btnTambahBaru.addActionListener(e -> {
            kosongkanForm();
        });

        btnHapus.addActionListener(e -> {
            if (txtIdAnggota.getText().isEmpty() || txtIdAnggota.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel!");
                return;
            }
            int konfirmasi = JOptionPane.showConfirmDialog(this, 
                                "Yakin ingin menghapus data ID " + txtIdAnggota.getText() + "?", 
                                "Konfirmasi Hapus", 
                                JOptionPane.YES_NO_OPTION);
            
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Anggota a = new Anggota().getById(Integer.parseInt(txtIdAnggota.getText()));
                a.delete();
                kosongkanForm();
                tampilkanData();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            }
        });

        btnCari.addActionListener(e -> {
            cari(txtCari.getText());
        });

        tblAnggota.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblAnggota.getSelectedRow() != -1) {
                    int row = tblAnggota.getSelectedRow();
                    int id = (int) tblAnggota.getModel().getValueAt(row, 0); 
                    
                    Anggota a = new Anggota().getById(id);
                    
                    txtIdAnggota.setText(String.valueOf(a.getIdanggota()));
                    txtNama.setText(a.getNama());
                    txtAlamat.setText(a.getAlamat());
                    txtTelepon.setText(a.getTelepon());
                }
            }
        });

        setVisible(true);
    }

    public void kosongkanForm() {
        txtIdAnggota.setText("0");
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
    }

    public final void tampilkanData() {
        String[] kolom = {"ID", "Nama", "Alamat", "Telepon"};
        ArrayList<Anggota> list = new Anggota().getAll();
        Object rowData[] = new Object[4];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Anggota a : list) {
            rowData[0] = a.getIdanggota();
            rowData[1] = a.getNama();
            rowData[2] = a.getAlamat();
            rowData[3] = a.getTelepon();

            model.addRow(rowData);
        }

        tblAnggota.setModel(model);
    }

    public final void cari(String keyword) {
        String[] kolom = {"ID", "Nama", "Alamat", "Telepon"};
        ArrayList<Anggota> list = new Anggota().search(keyword);
        Object rowData[] = new Object[4];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Anggota a : list) {
            rowData[0] = a.getIdanggota();
            rowData[1] = a.getNama();
            rowData[2] = a.getAlamat();
            rowData[3] = a.getTelepon();

            model.addRow(rowData);
        }

        tblAnggota.setModel(model);
    }
}