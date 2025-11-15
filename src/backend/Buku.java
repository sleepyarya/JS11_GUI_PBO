package backend;

import java.sql.*;
import java.util.ArrayList;

public class Buku {
    private int idbuku;
    private Kategori kategori; // Relasi ke class Kategori
    private String judul;
    private String penerbit;
    private String penulis;

    public Buku() {
    }

    public Buku(Kategori kategori, String judul, String penerbit, String penulis) {
        this.kategori = kategori;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    // --- Getter dan Setter ---
    public int getIdbuku() {
        return idbuku;
    }

    public void setIdbuku(int idbuku) {
        this.idbuku = idbuku;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }
    // -------------------------

    // Method untuk mengambil data buku berdasarkan ID
    public Buku getById(int id) {
        Buku buku = new Buku();
        ResultSet rs = DBHelper.selectQuery("SELECT "
                + " b.idbuku AS idbuku, "
                + " b.judul AS judul, "
                + " b.penerbit AS penerbit, "
                + " b.penulis AS penulis, "
                + " k.idkategori AS idkategori, "
                + " k.nama AS nama, "
                + " k.keterangan AS keterangan "
                + " FROM buku b "
                + " LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + " WHERE b.idbuku = " + id);

        try {
            if (rs.next()) {
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                
                // Ambil data Kategori dan set ke objek Buku
                Kategori kategori = new Kategori();
                kategori.setIdkategori(rs.getInt("idkategori"));
                kategori.setNama(rs.getString("nama"));
                kategori.setKeterangan(rs.getString("keterangan"));
                
                buku.setKategori(kategori);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buku;
    }

    // Method untuk mengambil semua data buku
    public ArrayList<Buku> getAll() {
        ArrayList<Buku> ListBuku = new ArrayList();
        ResultSet rs = DBHelper.selectQuery("SELECT "
                + " b.idbuku AS idbuku, "
                + " b.judul AS judul, "
                + " b.penerbit AS penerbit, "
                + " b.penulis AS penulis, "
                + " k.idkategori AS idkategori, "
                + " k.nama AS nama, "
                + " k.keterangan AS keterangan "
                + " FROM buku b "
                + " LEFT JOIN kategori k ON b.idkategori = k.idkategori ");

        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                
                // Ambil data Kategori dan set ke objek Buku
                Kategori kategori = new Kategori();
                kategori.setIdkategori(rs.getInt("idkategori"));
                kategori.setNama(rs.getString("nama"));
                kategori.setKeterangan(rs.getString("keterangan"));
                
                buku.setKategori(kategori);
                ListBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListBuku;
    }

    // Method untuk mencari data buku berdasarkan keyword
    public ArrayList<Buku> search(String keyword) {
        ArrayList<Buku> ListBuku = new ArrayList();
        
        String query = "SELECT "
                + " b.idbuku AS idbuku, "
                + " b.judul AS judul, "
                + " b.penerbit AS penerbit, "
                + " b.penulis AS penulis, "
                + " k.idkategori AS idkategori, "
                + " k.nama AS nama, "
                + " k.keterangan AS keterangan "
                + " FROM buku b "
                + " LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + " WHERE b.judul LIKE '%" + keyword + "%' "
                + " OR b.penerbit LIKE '%" + keyword + "%' "
                + " OR b.penulis LIKE '%" + keyword + "%' "
                + " OR k.nama LIKE '%" + keyword + "%' ";

        ResultSet rs = DBHelper.selectQuery(query);

        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdbuku(rs.getInt("idbuku"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                
                // Ambil data Kategori dan set ke objek Buku
                Kategori kategori = new Kategori();
                kategori.setIdkategori(rs.getInt("idkategori"));
                kategori.setNama(rs.getString("nama"));
                kategori.setKeterangan(rs.getString("keterangan"));
                
                buku.setKategori(kategori);
                ListBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListBuku;
    }

    // Method untuk menyimpan data (Insert/Update)
    public void save() {
        if (getById(idbuku).getIdbuku() == 0) { // INSERT
            String SQL = "INSERT INTO buku (idkategori, judul, penerbit, penulis) VALUES ("
                    + "'" + this.getKategori().getIdkategori() + "',"
                    + "'" + this.judul + "',"
                    + "'" + this.penerbit + "',"
                    + "'" + this.penulis + "'"
                    + ")";
            this.idbuku = DBHelper.insertQueryGetId(SQL);
        } else { // UPDATE
            String SQL = "UPDATE buku SET"
                    + " idkategori = '" + this.getKategori().getIdkategori() + "',"
                    + " judul = '" + this.judul + "',"
                    + " penerbit = '" + this.penerbit + "',"
                    + " penulis = '" + this.penulis + "'"
                    + " WHERE idbuku = " + this.idbuku;
            DBHelper.executeQuery(SQL);
        }
    }

    // Method untuk menghapus data
    public void delete() {
        String SQL = "DELETE FROM buku WHERE idbuku = " + this.idbuku;
        DBHelper.executeQuery(SQL);
    }
}