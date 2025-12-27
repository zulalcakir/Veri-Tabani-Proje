package com.example.demo.controller;

import com.example.demo.entity.Kategori;
import com.example.demo.service.KategoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kategoriler")
@CrossOrigin("*")
public class KategoriController {

    @Autowired
    private KategoriService kategoriService;

    // Tüm kategorileri getir
    @GetMapping
    public List<Kategori> getAll() {
        return kategoriService.tumKategoriler();
    }

    // Yeni kategori kaydet
    @PostMapping
    public Kategori save(@RequestBody Kategori kategori) {
        return kategoriService.kategoriKaydet(kategori);
    }

    // Kategori Güncelle (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Kategori> update(@PathVariable Integer id, @RequestBody Kategori kategoriDetaylari) {
        Kategori mevcutKategori = kategoriService.kategoriGetir(id);
        if (mevcutKategori != null) {
            mevcutKategori.setAd(kategoriDetaylari.getAd());
            Kategori guncellenenKategori = kategoriService.kategoriKaydet(mevcutKategori);
            return ResponseEntity.ok(guncellenenKategori);
        }
        return ResponseEntity.notFound().build();
    }

    // Kategori Sil
    // Mesajları JSON formatında (Map.of) dönüyoruz ki Frontend daha kolay okusun
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            kategoriService.kategoriSil(id);
            return ResponseEntity.ok(Map.of("message", "Kategori başarıyla silindi."));
        } catch (Exception e) {
            // Foreign Key (Yabancı Anahtar) kısıtlaması nedeniyle silinememe durumunu yakalıyoruz
            return ResponseEntity.badRequest().body(Map.of("message",
                    "Hata: Bu kategoriye ait kitaplar olduğu için silinemez. Önce o kitapları silmelisiniz."));
        }
    }
}