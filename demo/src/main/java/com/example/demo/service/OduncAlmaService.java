package com.example.demo.service;

import com.example.demo.entity.Kitap;
import com.example.demo.entity.Kullanici;
import com.example.demo.entity.OduncAlma;
import com.example.demo.repository.KitapRepository;
import com.example.demo.repository.KullaniciRepository;
import com.example.demo.repository.OduncAlmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OduncAlmaService {

    @Autowired
    private OduncAlmaRepository oduncAlmaRepository;

    @Autowired
    private KitapRepository kitapRepository;

    @Autowired
    private KullaniciRepository kullaniciRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public OduncAlma kitapOduncVer(OduncAlma odunc) {
        Kullanici kullanici = kullaniciRepository.findById(odunc.getKullanici().getId())
                .orElseThrow(() -> new RuntimeException("Hata: Kullanıcı bulunamadı."));

        Kitap kitap = kitapRepository.findById(odunc.getKitap().getId())
                .orElseThrow(() -> new RuntimeException("Hata: Kitap bulunamadı."));

        if (kitap.getStokAdedi() <= 0) {
            throw new RuntimeException("Hata: " + kitap.getKitapAdi() + " stokta kalmadı!");
        }

        kitap.setStokAdedi(kitap.getStokAdedi() - 1);
        kitapRepository.save(kitap);

        odunc.setKullanici(kullanici);
        odunc.setKitap(kitap);
        odunc.setOduncTarihi(LocalDateTime.now());

        // TEST İÇİN: Vade 1 dakika sonrası
        odunc.setVadeTarihi(LocalDateTime.now().plusMinutes(1));
        odunc.setIadeEdildiMi(false);
        odunc.setHesaplananCeza(0.0); // Başlangıçta ceza 0

        return oduncAlmaRepository.save(odunc);
    }

    @Transactional
    public OduncAlma kitapIadeAl(Integer oduncId) {
        OduncAlma kayit = oduncAlmaRepository.findById(oduncId)
                .orElseThrow(() -> new RuntimeException("Hata: Ödünç alma kaydı bulunamadı."));

        if (Boolean.TRUE.equals(kayit.getIadeEdildiMi())) {
            throw new RuntimeException("Hata: Bu kitap zaten iade edilmiş.");
        }

        // İADE ANINDA SON BİR KEZ CEZA HESAPLA (Net rakam için)
        cezaHesapla(kayit);

        Kitap kitap = kayit.getKitap();
        kitap.setStokAdedi(kitap.getStokAdedi() + 1);
        kitapRepository.save(kitap);

        kayit.setIadeEdildiMi(true);
        kayit.setTeslimTarihi(LocalDateTime.now());
        return oduncAlmaRepository.save(kayit);
    }

    /**
     * CEZA HESAPLAMA MOTORU (Dakika Bazlı Test Modu)
     */
    private void cezaHesapla(OduncAlma odunc) {
        LocalDateTime simdi = LocalDateTime.now();
        if (simdi.isAfter(odunc.getVadeTarihi())) {
            // Vade ile şu an arasındaki farkı dakika olarak al
            long gecikenDakika = Duration.between(odunc.getVadeTarihi(), simdi).toMinutes();
            if (gecikenDakika <= 0) gecikenDakika = 1; // 1 saniye bile geçse 1 dakika say

            // Kitap entity'sindeki günlükCeza değerini dakika cezası gibi kullanıyoruz (Hızlı test için)
            double birimCeza = odunc.getKitap().getGunlukCeza() != null ? odunc.getKitap().getGunlukCeza() : 5.0;
            double toplamCeza = gecikenDakika * birimCeza;

            odunc.setHesaplananCeza(toplamCeza);
        }
    }

    /**
     * OTOMATİK GÜNCELLEME VE BİLDİRİM
     * Her dakikanın 0. saniyesinde tüm aktif cezaları yeniden hesaplar.
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void gecikenleriKontrolEtVeBildir() {
        List<OduncAlma> aktifOduncler = oduncAlmaRepository.findByIadeEdildiMiFalse();

        for (OduncAlma odunc : aktifOduncler) {
            // Önce cezayı güncelle
            cezaHesapla(odunc);
            oduncAlmaRepository.save(odunc); // Veritabanına yaz ki JS görsün

            double guncelCeza = odunc.getHesaplananCeza();

            if (guncelCeza > 0) {
                try {
                    emailService.cezaBildirimiGonder(
                            odunc.getKullanici().getEmail(),
                            odunc.getKullanici().getAdSoyad(),
                            odunc.getKitap().getKitapAdi(),
                            guncelCeza
                    );
                } catch (Exception e) {
                    System.err.println("Mail hatası: " + e.getMessage());
                }
            }
        }
    }

    public List<OduncAlma> tumAktifOduncleriGetir() {
        // Liste çekilmeden önce hepsini anlık güncelle ki kullanıcı güncel cezayı görsün
        List<OduncAlma> liste = oduncAlmaRepository.findByIadeEdildiMiFalse();
        for(OduncAlma o : liste) {
            cezaHesapla(o);
        }
        return liste;
    }
}