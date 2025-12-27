package com.example.demo.controller;

import com.example.demo.entity.Kullanici;
import com.example.demo.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Kullanici loginBilgileri) {
        // Log: Gelen isteği takip et
        System.out.println("Giriş denemesi yapılıyor: " + loginBilgileri.getEmail());

        // 1. Kullanıcıyı sadece e-posta ile bul
        Kullanici kullanici = kullaniciRepository.findByEmail(loginBilgileri.getEmail()).orElse(null);

        if (kullanici != null) {
            // --- GEÇİCİ OLARAK ŞİFRE KONTROLÜNÜ KALDIRDIK ---
            // Amaç: Veritabanı ve frontend arasındaki bağlantının çalıştığını kanıtlamak

            // Rolü temizle ve ADMIN/OGRENCI formatına sok
            if (kullanici.getRol() == null || kullanici.getRol().trim().isEmpty()) {
                kullanici.setRol("OGRENCI");
            } else {
                kullanici.setRol(kullanici.getRol().trim().toUpperCase());
            }

            System.out.println("Giriş ONAYLANDI (Şifresiz): " + kullanici.getEmail() + " | Rol: " + kullanici.getRol());

            // Kullanıcı nesnesini döndür
            return ResponseEntity.ok(kullanici);
        }

        // Kullanıcı hiç bulunamazsa
        System.out.println("Giriş Başarısız: Veritabanında bu mail yok!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "E-posta veritabanında kayıtlı değil!"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Kullanici yeniKullanici) {
        if (kullaniciRepository.findByEmail(yeniKullanici.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Bu e-posta zaten kullanımda!"));
        }

        if (yeniKullanici.getRol() == null || yeniKullanici.getRol().trim().isEmpty()) {
            yeniKullanici.setRol("OGRENCI");
        } else {
            yeniKullanici.setRol(yeniKullanici.getRol().trim().toUpperCase());
        }

        kullaniciRepository.save(yeniKullanici);
        return ResponseEntity.ok(Map.of("message", "Kayıt başarıyla tamamlandı!"));
    }
}