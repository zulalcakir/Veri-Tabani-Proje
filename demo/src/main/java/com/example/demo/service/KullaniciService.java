package com.example.demo.service;

import com.example.demo.entity.Kullanici;
import com.example.demo.repository.KullaniciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class KullaniciService {

    @Autowired
    private KullaniciRepository kullaniciRepository;

    /**
     * KULLANICI KAYIT / GÜNCELLEME
     */
    public Kullanici kayitEt(Kullanici kullanici) {
        return kullaniciRepository.save(kullanici);
    }

    /**
     * TÜM KULLANICILARI LİSTELE (Admin Paneli İçin)
     */
    public List<Kullanici> tumKullanicilar() {
        return kullaniciRepository.findAll();
    }

    /**
     * EMAIL İLE KULLANICI BUL (Giriş İşlemleri İçin)
     */
    public Kullanici emailIleBul(String email) {
        return kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Hata: " + email + " adresine sahip kullanıcı bulunamadı."));
    }

    /**
     * KULLANICI SİL
     * Not: @Transactional ekledik ki silme sırasında bir hata olursa veritabanı tutarlı kalsın.
     */
    @Transactional
    public void kullaniciSil(Integer id) {
        if (!kullaniciRepository.existsById(id)) {
            throw new RuntimeException("Hata: Silinmek istenen kullanıcı sistemde kayıtlı değil.");
        }
        // Eğer kullanıcının üzerinde iade edilmemiş kitap varsa SQL kısıtlaması (FK) hata verir.
        // Controller bu hatayı yakalayıp ekrana güzel bir mesaj basacak.
        kullaniciRepository.deleteById(id);
    }

    /**
     * ROL GÜNCELLE (ADMIN / USER Değişimi)
     */
    @Transactional
    public Kullanici rolGuncelle(Integer id, String yeniRol) {
        Kullanici kullanici = kullaniciRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hata: Kullanıcı bulunamadı."));

        // Gelen rolü büyük harfe çevirip set ediyoruz (ADMIN/USER)
        kullanici.setRol(yeniRol.trim().toUpperCase());
        return kullaniciRepository.save(kullanici);
    }

    /**
     * KULLANICI SAYISI (Dashboard İstatistiği İçin)
     */
    public long toplamKullaniciSayisi() {
        return kullaniciRepository.count();
    }
}