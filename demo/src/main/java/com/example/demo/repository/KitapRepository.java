package com.example.demo.repository;

import com.example.demo.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KitapRepository extends JpaRepository<Kitap, Integer> {
    // Kitap adına göre arama
    List<Kitap> findByKitapAdiContainingIgnoreCase(String kitapAdi);

    // Belirli bir kategoriye ait kitapları listeleme
    List<Kitap> findByKategoriId(Integer kategoriId);

    // Stokta olan kitapları getir
    List<Kitap> findByStokAdediGreaterThan(Integer miktar);
}