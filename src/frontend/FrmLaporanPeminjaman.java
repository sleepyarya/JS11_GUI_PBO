package frontend;

import backend.Peminjaman;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmLaporanPeminjaman extends JFrame {

    private JTable tblLaporan;
    private JScrollPane scrollPane;
    private JButton btnRefresh;

    public FrmLaporanPeminjaman() {
        initComponents();
        tampilkanData();
    }

    private void initComponents() {
        setTitle("Laporan Peminjaman Aktif");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Aksi (NORTH) ---
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnRefresh = new JButton("Refresh Data");
        panelAksi.add(btnRefresh);

        add(panelAksi, BorderLayout.NORTH);

        // --- Tabel Laporan (CENTER) ---
        tblLaporan = new JTable();
        scrollPane = new JScrollPane(tblLaporan);

        add(scrollPane, BorderLayout.CENTER);

        // --- Event Listener ---
        btnRefresh.addActionListener(e -> {
            tampilkanData();
        });

        setVisible(true);
    }

    public final void tampilkanData() {
        String[] kolom = {"ID Pinjam", "Nama Anggota", "ID Anggota", "Judul Buku", "ID Buku", "Tgl Pinjam", "Tgl Kembali"};
        ArrayList<Peminjaman> list = new Peminjaman().getAll();
        Object rowData[] = new Object[7];

        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Peminjaman p : list) {
            rowData[0] = p.getIdpeminjaman();
            rowData[1] = p.getAnggota().getNama();
            rowData[2] = p.getAnggota().getIdanggota();
            rowData[3] = p.getBuku().getJudul();
            rowData[4] = p.getBuku().getIdbuku();
            rowData[5] = p.getTanggalPinjam();
            rowData[6] = p.getTanggalKembali();

            model.addRow(rowData);
        }

        tblLaporan.setModel(model);
        JOptionPane.showMessageDialog(this, "Data laporan berhasil diperbarui.");
    }
}