package com.example.demo.repository;

import com.example.demo.entity.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, Integer> {

    // 1. Ad veya Soyada göre arama (Büyük/Küçük harf duyarsız)
    List<Yazar> findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(String ad, String soyad);

    // 2. Tam ad ile arama
    List<Yazar> findByAdAndSoyad(String ad, String soyad);

    // 3. Özel Sorgu (JPQL): Belirli bir harf ile başlayan yazarları getir
    @Query("SELECT y FROM Yazar y WHERE y.ad LIKE :harf%")
    List<Yazar> ismiIleBaslayanlar(@Param("harf") String harf);

    // 4. Karmaşık Sorgu: En çok kitabı olan yazarları bulmak için ileride
    // Service katmanında kullanılacak temel metodlar burada tanımlanır.
    List<Yazar> findAllByOrderByAdAsc(); // Tüm yazarları alfabetik sırala
}