package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "kullanicilar")
@Getter
@Setter
public class Kullanici {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kullanici_id")
    private Integer id;

    @Column(name = "ad_soyad")
    private String adSoyad;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "sifre")
    @JsonIgnore // Güvenlik için şifre bilgisini API yanıtlarında gizleriz
    private String sifre;

    @Column(name = "rol")
    private String rol;

    // --- SONSUZ DÖNGÜYÜ KIRAN KISIMLAR ---

    @OneToMany(mappedBy = "kullanici")
    @JsonIgnore // Kullanıcıyı çekerken ödünç aldığı kitapların detayına girmesini engeller
    private List<OduncAlma> oduncAldigiKitaplar;

    @OneToMany(mappedBy = "kullanici")
    @JsonIgnore // Kullanıcıyı çekerken ceza listesini JSON'a dahil etmez
    private List<Ceza> cezalar;
}