package backend;

import java.sql.*;
import java.time.LocalDate; 
import java.util.ArrayList; 

public class Peminjaman {
    private int idpeminjaman;
    private Anggota anggota; 
    private Buku buku;       
    private String tanggalPinjam;
    private String tanggalKembali;

    public Peminjaman() {
        // Set tanggal pinjam default hari ini (YYYY-MM-DD)
        this.tanggalPinjam = LocalDate.now().toString(); 
        // Set tanggal kembali default 7 hari dari sekarang
        this.tanggalKembali = LocalDate.now().plusDays(7).toString();
    }
    
    public Peminjaman(Anggota anggota, Buku buku, String tanggalPinjam, String tanggalKembali) {
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
    }

    // --- Getter dan Setter ---
    public int getIdpeminjaman() { return idpeminjaman; }
    public void setIdpeminjaman(int idpeminjaman) { this.idpeminjaman = idpeminjaman; }
    
    public Anggota getAnggota() { return anggota; }
    public void setAnggota(Anggota anggota) { this.anggota = anggota; }
    
    public Buku getBuku() { return buku; }
    public void setBuku(Buku buku) { this.buku = buku; }
    
    public String getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(String tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }
    
    public String getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(String tanggalKembali) { this.tanggalKembali = tanggalKembali; }
    // -------------------------

    public Peminjaman getById(int id) {
        Peminjaman pinjam = new Peminjaman();
        String query = "SELECT "
                + " p.idpeminjaman AS idpeminjaman, "
                + " p.tanggalpinjam AS tanggalpinjam, "
                + " p.tanggal_kembali AS tanggal_kembali, "
                + " a.idanggota AS idanggota, a.nama AS namaanggota, a.alamat AS alamat, a.telepon AS telepon, "
                + " b.idbuku AS idbuku, b.judul AS judul, b.penerbit AS penerbit, b.penulis AS penulis, b.idkategori AS idkategori "
                + " FROM peminjaman p "
                + " LEFT JOIN anggota a ON p.idanggota = a.idanggota "
                + " LEFT JOIN buku b ON p.idbuku = b.idbuku "
                + " WHERE p.idpeminjaman = " + id;
        
        ResultSet rs = DBHelper.selectQuery(query);

        try {
            if (rs.next()) {
                // Set data Peminjaman
                pinjam.setIdpeminjaman(rs.getInt("idpeminjaman"));
                pinjam.setTanggalPinjam(rs.getString("tanggalpinjam"));
                pinjam.setTanggalKembali(rs.getString("tanggal_kembali"));
                
                // Set data Anggota
                Anggota anggota = new Anggota();
                anggota.setIdanggota(rs.getInt("idanggota"));
                anggota.setNama(rs.getString("namaanggota"));
                anggota.setAlamat(rs.getString("alamat"));
                anggota.setTelepon(rs.getString("telepon"));
                pinjam.setAnggota(anggota);
                
                // Set data Buku (Menggunakan getById dari Buku untuk melengkapi Kategori)
                Buku buku = new Buku().getById(rs.getInt("idbuku"));
                pinjam.setBuku(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinjam;
    }

    public ArrayList<Peminjaman> getAll() {
        ArrayList<Peminjaman> ListPeminjaman = new ArrayList();
        String query = "SELECT "
                + " p.idpeminjaman AS idpeminjaman, "
                + " p.tanggalpinjam AS tanggalpinjam, "
                + " p.tanggal_kembali AS tanggal_kembali, "
                + " a.idanggota AS idanggota, a.nama AS namaanggota, a.alamat AS alamat, a.telepon AS telepon, "
                + " b.idbuku AS idbuku, b.judul AS judul, b.penerbit AS penerbit, b.penulis AS penulis, b.idkategori AS idkategori "
                + " FROM peminjaman p "
                + " LEFT JOIN anggota a ON p.idanggota = a.idanggota "
                + " LEFT JOIN buku b ON p.idbuku = b.idbuku "
                + " ORDER BY p.tanggalpinjam DESC";

        ResultSet rs = DBHelper.selectQuery(query);

        try {
            while (rs.next()) {
                Peminjaman pinjam = new Peminjaman();
                
                // Set data Peminjaman
                pinjam.setIdpeminjaman(rs.getInt("idpeminjaman"));
                pinjam.setTanggalPinjam(rs.getString("tanggalpinjam"));
                pinjam.setTanggalKembali(rs.getString("tanggal_kembali"));
                
                // Set data Anggota
                Anggota anggota = new Anggota();
                anggota.setIdanggota(rs.getInt("idanggota"));
                anggota.setNama(rs.getString("namaanggota"));
                anggota.setAlamat(rs.getString("alamat"));
                anggota.setTelepon(rs.getString("telepon"));
                pinjam.setAnggota(anggota);
                
                // Set data Buku (Menggunakan getById dari Buku untuk melengkapi Kategori)
                Buku buku = new Buku().getById(rs.getInt("idbuku"));
                pinjam.setBuku(buku);

                ListPeminjaman.add(pinjam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListPeminjaman;
    }

    public ArrayList<Peminjaman> search(String keyword) {
        ArrayList<Peminjaman> ListPeminjaman = new ArrayList();
        String query = "SELECT "
                + " p.idpeminjaman AS idpeminjaman, "
                + " p.tanggalpinjam AS tanggalpinjam, "
                + " p.tanggal_kembali AS tanggal_kembali, "
                + " a.idanggota AS idanggota, a.nama AS namaanggota, "
                + " b.idbuku AS idbuku, b.judul AS judul "
                + " FROM peminjaman p "
                + " LEFT JOIN anggota a ON p.idanggota = a.idanggota "
                + " LEFT JOIN buku b ON p.idbuku = b.idbuku "
                + " WHERE a.nama LIKE '%" + keyword + "%' "
                + " OR b.judul LIKE '%" + keyword + "%' ";

        ResultSet rs = DBHelper.selectQuery(query);

        try {
             while (rs.next()) {
                Peminjaman pinjam = new Peminjaman();
                
                // Set data Peminjaman
                pinjam.setIdpeminjaman(rs.getInt("idpeminjaman"));
                pinjam.setTanggalPinjam(rs.getString("tanggalpinjam"));
                pinjam.setTanggalKembali(rs.getString("tanggal_kembali"));
                
                // Set data Anggota
                Anggota anggota = new Anggota().getById(rs.getInt("idanggota"));
                pinjam.setAnggota(anggota);
                
                // Set data Buku
                Buku buku = new Buku().getById(rs.getInt("idbuku"));
                pinjam.setBuku(buku);

                ListPeminjaman.add(pinjam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListPeminjaman;
    }

    public void save() {
        if (getById(idpeminjaman).getIdpeminjaman() == 0) { // INSERT
            String SQL = "INSERT INTO peminjaman (idanggota, idbuku, tanggalpinjam, tanggal_kembali) VALUES ("
                    + "'" + this.getAnggota().getIdanggota() + "',"
                    + "'" + this.getBuku().getIdbuku() + "',"
                    + "'" + this.tanggalPinjam + "',"
                    + "'" + this.tanggalKembali + "'"
                    + ")";
            this.idpeminjaman = DBHelper.insertQueryGetId(SQL);
        } else { // UPDATE 
            String SQL = "UPDATE peminjaman SET"
                    + " idanggota = '" + this.getAnggota().getIdanggota() + "',"
                    + " idbuku = '" + this.getBuku().getIdbuku() + "',"
                    + " tanggalpinjam = '" + this.tanggalPinjam + "',"
                    + " tanggal_kembali = '" + this.tanggalKembali + "'"
                    + " WHERE idpeminjaman = " + this.idpeminjaman;
            DBHelper.executeQuery(SQL);
        }
    }

    public void delete() {
        String SQL = "DELETE FROM peminjaman WHERE idpeminjaman = " + this.idpeminjaman;
        DBHelper.executeQuery(SQL);
    }
}