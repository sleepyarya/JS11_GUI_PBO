package backend;
import java.sql.*;
import java.util.ArrayList; 

public class Kategori {
    private int idkategori; 
    private String nama; 
    private String keterangan; 

    public Kategori() { 
    }
    
    public Kategori(String nama, String keterangan) {
        this.nama = nama; 
        this.keterangan = keterangan; 
    }

    // --- Getter dan Setter ---
    public int getIdkategori() { return idkategori; }
    public void setIdkategori(int idkategori) { this.idkategori = idkategori; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    // -------------------------

    private Kategori fillObject(ResultSet rs) {
        Kategori kat = new Kategori();
        try {
            while (rs.next()) { 
                kat.setIdkategori(rs.getInt("idkategori")); 
                kat.setNama(rs.getString("nama")); 
                kat.setKeterangan(rs.getString("keterangan")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kat;
    }

    public Kategori getById(int id) {
        Kategori kat = new Kategori(); 
        String query = "SELECT * FROM kategori WHERE idkategori = " + id;
        ResultSet rs = DBHelper.selectQuery(query); 
        
        try {
            if (rs.next()) { // Hanya perlu satu baris (idkategori adalah PK)
                kat.setIdkategori(rs.getInt("idkategori")); 
                kat.setNama(rs.getString("nama")); 
                kat.setKeterangan(rs.getString("keterangan")); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return kat; 
    }

    public ArrayList<Kategori> getAll() {
        ArrayList<Kategori> ListKategori = new ArrayList(); 
        String query = "SELECT * FROM kategori"; 
        ResultSet rs = DBHelper.selectQuery(query); 

        try { 
            while (rs.next()) { 
                Kategori kat = new Kategori(); 
                kat.setIdkategori(rs.getInt("idkategori")); 
                kat.setNama(rs.getString("nama")); 
                kat.setKeterangan(rs.getString("keterangan")); 
                ListKategori.add(kat); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return ListKategori; 
    }

    public ArrayList<Kategori> search(String keyword) {
        ArrayList<Kategori> ListKategori = new ArrayList(); 
        String sql = "SELECT * FROM kategori WHERE nama LIKE '%" + keyword + "%'"
                + " OR keterangan LIKE '%" + keyword + "%'"; 
        
        ResultSet rs = DBHelper.selectQuery(sql); 

        try { 
            while (rs.next()) { 
                Kategori kat = new Kategori(); 
                kat.setIdkategori(rs.getInt("idkategori")); 
                kat.setNama(rs.getString("nama")); 
                kat.setKeterangan(rs.getString("keterangan")); 
                ListKategori.add(kat); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return ListKategori; 
    }

    public void save() { 
        if (getById(idkategori).getIdkategori() == 0) { // INSERT
            String SQL = "INSERT INTO kategori (nama, keterangan) VALUES (" 
                    + "'" + this.nama + "'," 
                    + "'" + this.keterangan + "'" 
                    + ")"; 
            this.idkategori = DBHelper.insertQueryGetId(SQL); 
        } else { // UPDATE
            String SQL = "UPDATE kategori SET" 
                    + " nama = '" + this.nama + "'," 
                    + " keterangan = '" + this.keterangan + "'" 
                    + " WHERE idkategori = " + this.idkategori; 
            DBHelper.executeQuery(SQL); 
        }
    }
    
    public void delete() { 
        String SQL = "DELETE FROM kategori WHERE idkategori = " + this.idkategori; 
        DBHelper.executeQuery(SQL); 
    }
    
    @Override
    public String toString() {
        return this.nama;
        
    }
}