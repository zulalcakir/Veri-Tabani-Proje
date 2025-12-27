package com.example.demo.repository;

import com.example.demo.entity.Ceza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CezaRepository extends JpaRepository<Ceza, Integer> {
    // Bir kullanıcının tüm cezaları
    List<Ceza> findByKullaniciId(Integer kullaniciId);

    // Sadece ödenmemiş cezalar
    List<Ceza> findByOdendiMiFalse();
}