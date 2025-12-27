package com.example.demo.repository;

import com.example.demo.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Integer> {

    // Giriş işlemleri için en temiz yöntem (Entity'deki değişken adın 'email' olduğu için)
    Optional<Kullanici> findByEmail(String email);

    // İsim araması için (Entity'deki değişken adın 'adSoyad' olduğu için buna uydurduk)
    List<Kullanici> findByAdSoyadContainingIgnoreCase(String adSoyad);
}
