package com.example.demo.repository;

import com.example.demo.entity.OduncAlma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OduncAlmaRepository extends JpaRepository<OduncAlma, Integer> {
    // Bir kullanıcının aldığı tüm ödünç kayıtları
    List<OduncAlma> findByKullaniciId(Integer kullaniciId);

    // Henüz iade edilmemiş (eldeki) kitaplar
    List<OduncAlma> findByIadeEdildiMiFalse();

    // Belirli bir kitabın ödünç geçmişi
    List<OduncAlma> findByKitapId(Integer kitapId);
}