package com.example.demo.service;

import com.example.demo.entity.Kategori;
import com.example.demo.entity.Kitap;
import com.example.demo.repository.KategoriRepository;
import com.example.demo.repository.KitapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class KitapService {

    @Autowired
    private KitapRepository kitapRepository;

    // HATAYI ÇÖZMEK İÇİN GEREKLİ REPOSITORY BAĞLANTISI
    @Autowired
    private KategoriRepository kategoriRepository;

    // Tüm kitapları getirir
    public List<Kitap> tumKitaplar() {
        return kitapRepository.findAll();
    }

    // ID'ye göre tek bir kitap bulur
    public Kitap kitapBul(Integer id) {
        return kitapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı! ID: " + id));
    }

    // --- KRİTİK GÜNCELLEME: YENİ KİTAP KAYDETME MANTIĞI ---
    @Transactional
    public Kitap kitapKaydet(Kitap kitap) {
        // Eğer kitaba bağlı bir kategori bilgisi gelmişse (Frontend'den gelen kategoriId)
        if (kitap.getKategori() != null && kitap.getKategori().getId() != null) {

            // Veritabanından bu ID'ye sahip GERÇEK kategori nesnesini çekiyoruz
            Kategori mevcutKategori = kategoriRepository.findById(kitap.getKategori().getId())
                    .orElseThrow(() -> new RuntimeException("Seçilen kategori sistemde bulunamadı!"));

            // Kitaba, Hibernate'in tanıdığı o mühürlü/kayıtlı nesneyi bağlıyoruz
            kitap.setKategori(mevcutKategori);
        }

        // Artık Hibernate "bu kategori bende kayıtlı" diyecek ve 500 hatası vermeyecek
        return kitapRepository.save(kitap);
    }

    // Kitabı sistemden siler
    public void kitapSil(Integer id) {
        kitapRepository.deleteById(id);
    }

    // İsme göre arama yapmak istersen
    public List<Kitap> ismeGoreAra(String isim) {
        return kitapRepository.findByKitapAdiContainingIgnoreCase(isim);
    }
}