package com.example.demo.repository;

import com.example.demo.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Integer> {

    // 1. Kategorileri alfabeye göre sıralı getirir (A-Z)
    List<Kategori> findAllByOrderByAdAsc();

    // 2. Kategori adına göre tam eşleşme arar (Yeni kategori eklerken mükerrer kaydı önlemek için)
    Optional<Kategori> findByAdIgnoreCase(String ad);

    // 3. Kategori adı içerenleri bulur (Kategori arama çubuğu yaparsan lazım olur)
    List<Kategori> findByAdContainingIgnoreCase(String ad);

    // 4. Kategori var mı yok mu kontrolü
    boolean existsByAdIgnoreCase(String ad);
}