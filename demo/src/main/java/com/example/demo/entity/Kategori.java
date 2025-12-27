package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "kategoriler") // Veritabanındaki tablo adıyla birebir aynı olmalı
@Getter
@Setter
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kategori_id") // SQL'deki 'id' sütunuyla birebir eşleşmeli
    private Integer id;

    @Column(name = "ad", nullable = false)
    private String ad;

    // Bir kategoride birden fazla kitap olabilir (İlişki tanımı)
    @OneToMany(mappedBy = "kategori", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Kitap> kitaplar;
}