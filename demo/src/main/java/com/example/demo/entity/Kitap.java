package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "kitaplar")
@Data
public class Kitap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitap_id")
    private Integer id;

    @Column(name = "kitap_adi", nullable = false)
    private String kitapAdi;

    @Column(name = "yazar")
    private String yazar;

    @Column(name = "stok_adedi")
    private Integer stokAdedi;

    // ADMIN TARAFINDAN BELİRLENECEK GÜNLÜK CEZA TUTARI
    @Column(name = "gunluk_ceza")
    private Double gunlukCeza;

    @ManyToOne
    @JoinColumn(name = "kategori_id")
    @JsonIgnoreProperties("kitaplar")
    private Kategori kategori;

    @OneToMany(mappedBy = "kitap", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OduncAlma> oduncIslemleri;
}