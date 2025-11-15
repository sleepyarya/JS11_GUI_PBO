package frontend;
import backend.*;
import java.util.List;
import javax.swing.SwingUtilities; 

public class TestBackend {
    public static void main(String[] args) { 
        
        // -------------------------------------------------------------------------------------------------------
        // --- 1. TESTING BACKEND (CRUD KATEGORI) ---
        // -------------------------------------------------------------------------------------------------------
        System.out.println("\n--- TESTING BACKEND (CRUD KATEGORI) ---");
        // (Anda dapat menambahkan pengujian Kategori yang aman di sini jika diperlukan)
        
        
        // -------------------------------------------------------------------------------------------------------
        // --- 2. TESTING BACKEND (CRUD BUKU) ---
        // -------------------------------------------------------------------------------------------------------
        System.out.println("\n--- TESTING BACKEND (CRUD BUKU) ---");

        // Ambil objek Kategori yang sudah ada dari database (Perlu untuk pengujian Buku)
        List<Kategori> listNovel = new Kategori().search("Novel");
        Kategori novel = listNovel.isEmpty() ? null : listNovel.get(0);
        
        List<Kategori> listReferensi = new Kategori().search("Referensi");
        Kategori referensi = listReferensi.isEmpty() ? null : listReferensi.get(0);
        
        if (novel != null && referensi != null) {
            System.out.println("Ditemukan Kategori: " + novel.getNama() + " dan " + referensi.getNama());
            
            // ************ BARIS INSERT BUKU DIHAPUS (buku.save()) ************
            
            // KODE PENGUJIAN AMAN: Ambil data Buku yang sudah ada untuk di-update
            List<Buku> listBuku = new Buku().getAll(); 
            
            if (listBuku.size() >= 1) {
                Buku bukuToUpdate = listBuku.get(0); 

                // test update
                System.out.println("Updating Buku ID " + bukuToUpdate.getIdbuku() + "...");
                bukuToUpdate.setJudul("Aljabar Linier - UPDATED"); // Judul akan selalu berubah
                bukuToUpdate.save();
                
                // ************ BARIS DELETE BUKU DIHAPUS ************ } else {
                System.out.println("ℹ️ INFO: Tidak ada data Buku di database untuk diuji Update.");
            }
            
            // test select all
            System.out.println("Result after Buku operations (getAll):");
            for (Buku b : new Buku().getAll()) {
                System.out.println("ID: " + b.getIdbuku() + 
                                   ", Kategori: " + b.getKategori().getNama() + 
                                   ", Judul: " + b.getJudul() + 
                                   ", Penulis: " + b.getPenulis());
            }
            
            // test search
            System.out.println("Result after Buku search ('Linier'):");
            for (Buku b : new Buku().search("Linier")) {
                 System.out.println("ID: " + b.getIdbuku() + 
                                    ", Kategori: " + b.getKategori().getNama() + 
                                    ", Judul: " + b.getJudul() + 
                                    ", Penulis: " + b.getPenulis());
            }
            
        } else {
            System.out.println("❌ ERROR: Kategori 'Novel' atau 'Referensi' tidak ditemukan. Pastikan data Kategori sudah ada.");
        }

        // -------------------------------------------------------------------------------------------------------
        // --- 3. RUNNING FRONTEND (GUI) ---
        // -------------------------------------------------------------------------------------------------------
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Form Kategori
               // new FrmKategori(); 
                // Form Buku
               // new FrmBuku();
                //new FrmAnggota();
                //new FrmPeminjaman();
                new FrmMenu();
            }
        });
    }
}