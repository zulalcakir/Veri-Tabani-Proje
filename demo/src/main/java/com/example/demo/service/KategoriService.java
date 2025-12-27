package com.example.demo.service;

import com.example.demo.entity.Kategori;
import com.example.demo.repository.KategoriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class KategoriService {

    @Autowired
    private KategoriRepository kategoriRepository;

    /**
     * Tüm kategorileri alfabetik sırayla getirir.
     */
    public List<Kategori> tumKategoriler() {
        return kategoriRepository.findAll(Sort.by(Sort.Direction.ASC, "ad"));
    }

    /**
     * Yeni kategori kaydeder veya mevcut olanı günceller.
     */
    @Transactional
    public Kategori kategoriKaydet(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    /**
     * Kategori Silme İşlemi
     * ID kontrolü ve Transactional yapısı ile en güvenli halidir.
     */
    @Transactional
    public void kategoriSil(Integer id) {
        if (id == null) {
            throw new RuntimeException("Hata: Kategori ID boş (null) olamaz!");
        }

        // Önce veritabanında var mı diye bakıyoruz
        if (!kategoriRepository.existsById(id)) {
            throw new RuntimeException("Hata: Silinmek istenen kategori veritabanında bulunamadı. ID: " + id);
        }

        try {
            kategoriRepository.deleteById(id);
        } catch (Exception e) {
            // Eğer veritabanı seviyesinde bir kısıtlamaya (Foreign Key) takılırsa burası çalışır
            throw new RuntimeException("Hata: Bu kategoriye bağlı kitaplar olabilir, silme işlemi reddedildi.");
        }
    }

    /**
     * ID ile tek bir kategori getirir.
     */
    public Kategori kategoriGetir(Integer id) {
        return kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hata: Kategori bulunamadı. ID: " + id));
    }
}