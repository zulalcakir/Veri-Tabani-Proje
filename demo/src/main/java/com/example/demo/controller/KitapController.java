package com.example.demo.controller;

import com.example.demo.entity.Kitap;
import com.example.demo.service.KitapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kitaplar")
@CrossOrigin("*")
public class KitapController {

    @Autowired
    private KitapService kitapService;

    // --- TÜM KİTAPLARI GETİR ---
    @GetMapping
    public List<Kitap> getAll() {
        return kitapService.tumKitaplar();
    }

    // --- TEK BİR KİTABI GETİR ---
    @GetMapping("/{id}")
    public ResponseEntity<Kitap> getById(@PathVariable Integer id) {
        Kitap kitap = kitapService.kitapBul(id);
        if (kitap != null) {
            return ResponseEntity.ok(kitap);
        }
        return ResponseEntity.notFound().build();
    }

    // --- YENİ KİTAP EKLE VEYA GÜNCELLE ---
    @PostMapping
    public ResponseEntity<Kitap> save(@RequestBody Kitap kitap) {
        // Kitap eklenirken yazar, kategori ve stok bilgisi Kitap nesnesi içinde gelir
        Kitap kaydedilenKitap = kitapService.kitapKaydet(kitap);
        return ResponseEntity.ok(kaydedilenKitap);
    }

    // --- KİTAP SİL ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            kitapService.kitapSil(id);
            return ResponseEntity.ok(Map.of("message", "Kitap başarıyla silindi."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Kitap silinemedi: " + e.getMessage()));
        }
    }
}