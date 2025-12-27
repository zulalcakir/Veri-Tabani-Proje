package com.example.demo.controller;

import com.example.demo.entity.Kullanici;
import com.example.demo.repository.KullaniciRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/cezalar")
@CrossOrigin("*")
public class CezaController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @PostMapping("/kes/{kullaniciId}")
    public ResponseEntity<?> cezaKes(@PathVariable Integer kullaniciId, @RequestBody Map<String, Object> payload) {
        // Double miktar = (Double) payload.get("miktar"); // Object olarak alıp cast etmek daha güvenli
        Double miktar = Double.parseDouble(payload.get("miktar").toString());
        String kitapAdi = payload.containsKey("kitapAdi") ? payload.get("kitapAdi").toString() : "Genel Kütüphane Borcu";

        // 1. Kullanıcıyı bul
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId).orElse(null);

        if (kullanici == null) {
            return ResponseEntity.badRequest().body("Kullanıcı bulunamadı.");
        }

        // 2. E-posta gönder (4 parametre: Email, Ad Soyad, Kitap Adı, Miktar)
        // HATA ÇÖZÜMÜ: Buraya kitapAdi parametresi eklendi.
        emailService.cezaBildirimiGonder(kullanici.getEmail(), kullanici.getAdSoyad(), kitapAdi, miktar);

        return ResponseEntity.ok("Ceza başarıyla kesildi ve kullanıcıya e-posta gönderildi.");
    }
}