package com.example.demo.controller;

import com.example.demo.entity.Kullanici;
import com.example.demo.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kullanicilar")
@CrossOrigin("*")
public class KullaniciController {

    @Autowired
    private KullaniciService kullaniciService;

    // 1. TÜM KULLANICILARI LİSTELE
    @GetMapping
    public List<Kullanici> getAll() {
        return kullaniciService.tumKullanicilar();
    }

    // 2. EMAIL İLE KULLANICI BUL
    @GetMapping("/bul")
    public Kullanici getByEmail(@RequestParam String email) {
        return kullaniciService.emailIleBul(email);
    }

    // 3. KULLANICI SİL (Admin Panelinden Üye Atma)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            kullaniciService.kullaniciSil(id); // Service katmanında bu metot olmalı
            return ResponseEntity.ok(Map.of("message", "Kullanıcı başarıyla silindi."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Hata: Kullanıcı silinemedi. Üzerinde aktif ödünç kitap olabilir."));
        }
    }

    // 4. ROL GÜNCELLE (Üyeyi Admin Yapma veya Tersini Yapma)
    @PutMapping("/{id}/rol")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            String yeniRol = body.get("rol");
            Kullanici guncelKullanici = kullaniciService.rolGuncelle(id, yeniRol); // Service'e eklenmeli
            return ResponseEntity.ok(guncelKullanici);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rol güncellenemedi."));
        }
    }

    // 5. BASİT İSTATİSTİKLER (Dashboard için)
    @GetMapping("/count")
    public Long getUserCount() {
        return (long) kullaniciService.tumKullanicilar().size();
    }
}