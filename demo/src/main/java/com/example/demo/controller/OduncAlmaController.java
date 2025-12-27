package com.example.demo.controller;

import com.example.demo.entity.OduncAlma;
import com.example.demo.service.OduncAlmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/odunc-alma")
@CrossOrigin("*")
public class OduncAlmaController {

    @Autowired
    private OduncAlmaService oduncAlmaService;

    // Kitap Ödünç Verme İşlemi
    @PostMapping("/ver")
    public ResponseEntity<?> oduncVer(@RequestBody OduncAlma odunc) {
        try {
            return ResponseEntity.ok(oduncAlmaService.kitapOduncVer(odunc));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Borç Ödeme ve İade Alma (Finansal Paneldeki buton burayı tetikleyecek)
    @PutMapping("/iade/{id}")
    public ResponseEntity<?> iadeAl(@PathVariable Integer id) {
        try {
            OduncAlma guncellenen = oduncAlmaService.kitapIadeAl(id);
            return ResponseEntity.ok(Map.of(
                    "message", "İade işlemi başarılı ve borç kapatıldı.",
                    "data", guncellenen
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Aktif ödünçleri listeleme (Borçlar sayfasında kullanılır)
    @GetMapping("/aktif")
    public List<OduncAlma> aktifOduncleriListele() {
        return oduncAlmaService.tumAktifOduncleriGetir();
    }

    // --- YENİ: FİNANSAL PANEL İÇİN ÖZET VERİ ---
    // Paneldeki "Toplam Ceza" ve "İstatistikler" hatasını çözer.
    @GetMapping("/finansal-ozet")
    public ResponseEntity<?> getFinansalOzet() {
        List<OduncAlma> aktifler = oduncAlmaService.tumAktifOduncleriGetir();

        // Java Entity içindeki getHesaplananCeza metodunu kullanarak toplamı bulur
        double toplamCeza = aktifler.stream()
                .mapToDouble(OduncAlma::getHesaplananCeza)
                .sum();

        return ResponseEntity.ok(Map.of(
                "toplamCeza", toplamCeza,
                "aktifKitapSayisi", aktifler.size(),
                "gecikmisKitapSayisi", aktifler.stream().filter(o -> o.getHesaplananCeza() > 0).count()
        ));
    }
}