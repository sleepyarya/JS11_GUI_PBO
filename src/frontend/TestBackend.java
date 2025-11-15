package frontend;
import backend.*; 
import javax.swing.SwingUtilities;

public class TestBackend {
    public static void main(String[] args) { 
        
        // --- 1. TESTING BACKEND (CRUD KATEGORI) ---
        
        System.out.println("--- TESTING BACKEND (CRUD KATEGORI) ---");
        
        // Buat objek Kategori dengan sintaks konstruktor yang BENAR (tanpa named parameters)
        Kategori kat1 = new Kategori("Novel Fiksi", "Koleksi buku novel dan cerita fiksi"); 
        Kategori kat2 = new Kategori("Referensi", "Buku referensi ilmiah"); 
        Kategori kat3 = new Kategori("Komik", "Komik anak-anak dan manga"); 
        
        // test insert
        System.out.println("Inserting Kategori...");
        kat1.save(); 
        kat2.save(); 
        kat3.save(); 
        
        // test update
        System.out.println("Updating Kategori...");
        // Kita ubah kat2
        kat2.setKeterangan("Koleksi buku referensi dan riset ilmiah terbaru"); 
        kat2.save(); 
        
        // test delete
        System.out.println("Deleting Kategori (Komik)...");
        // Kita hapus kat3
        kat3.delete(); 
        
        // test select all
        System.out.println("Result after Kategori operations (getAll):");
        for (Kategori k : new Kategori().getAll()) { 
            System.out.println("ID: " + k.getIdkategori() + ", Nama: " + k.getNama() + ", Ket: " + k.getKeterangan()); 
        }
        
        // test search
        System.out.println("Result after Kategori search ('ilmiah'):");
        for (Kategori k : new Kategori().search("ilmiah")) { 
            System.out.println("ID: " + k.getIdkategori() + ", Nama: " + k.getNama() + ", Ket: " + k.getKeterangan()); 
        }
        
        
        // --- 2. RUNNING FRONTEND (GUI) ---
        
        // Panggil method untuk menjalankan GUI FrmKategori 
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // FrmKategori harus ada di src/frontend
                new FrmKategori();
            }
        });
    }
}