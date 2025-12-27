package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "odunc_alma")
@Data
public class OduncAlma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    @JsonIgnoreProperties({"oduncAldigiKitaplar", "cezalar", "sifre"})
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "kitap_id")
    @JsonIgnoreProperties("oduncIslemleri")
    private Kitap kitap;

    @Column(name = "odunc_tarihi")
    private LocalDateTime oduncTarihi;

    @Column(name = "vade_tarihi")
    private LocalDateTime vadeTarihi;

    @Column(name = "teslim_tarihi")
    private LocalDateTime teslimTarihi;

    @Column(name = "iade_edildi_mi")
    private Boolean iadeEdildiMi = false;

    // --- KRİTİK DEĞİŞİKLİK: Veritabanına kaydedilen ceza alanı ---
    @Column(name = "hesaplanan_ceza")
    private Double hesaplananCeza = 0.0;

    // --- GELİŞMİŞ CEZA HESAPLAMA MOTORU ---
    // @JsonProperty sayesinde Jackson bu metodu JSON'a "hesaplananCeza" olarak ekler
    // OduncAlma.java içerisinde ilgili metot

    @JsonProperty("hesaplananCeza") // Tarayıcının bu ismi tanımasını garanti eder
    public Double getHesaplananCeza() { // Metot ismini 'get' ile başlattık
        // Adminin kitap eklerken girdiği ceza miktarını al, yoksa 5.0 varsay
        double birimCeza = (kitap != null && kitap.getGunlukCeza() != null) ? kitap.getGunlukCeza() : 5.0;

        // 1. Durum: Kitap iade edildiyse
        if (Boolean.TRUE.equals(iadeEdildiMi)) {
            if (vadeTarihi == null || teslimTarihi == null) {
                return (this.hesaplananCeza != null ? this.hesaplananCeza : 0.0);
            }
            // Vade ile teslim anı arasındaki farkı dakika olarak hesapla
            long gecikenSure = java.time.temporal.ChronoUnit.MINUTES.between(vadeTarihi, teslimTarihi);
            return gecikenSure > 0 ? gecikenSure * birimCeza : 0.0;
        }

        // 2. Durum: Kitap hala kullanıcıdaysa (Anlık hesaplama)
        if (vadeTarihi == null) return 0.0;

        // Vade ile şu an arasındaki farkı dakika olarak hesapla
        long gecikenSure = java.time.temporal.ChronoUnit.MINUTES.between(vadeTarihi, java.time.LocalDateTime.now());
        double toplam = gecikenSure > 0 ? gecikenSure * birimCeza : 0.0;

        // ÖNEMLİ: Hesaplanan değeri sınıfın içindeki değişkene de yazıyoruz
        // Böylece veritabanına kaydedilirken son değer saklanır
        this.hesaplananCeza = toplam;

        return toplam;
    }

    // --- KALAN SÜRE HESAPLAMA ---
    @Transient
    @JsonProperty("kalanSure")
    public long getKalanSure() {
        if (Boolean.TRUE.equals(iadeEdildiMi) || vadeTarihi == null) return 0;
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), vadeTarihi);
    }

    // Servis katmanındaki hatayı çözen manuel Setter (Lombok bazen Transient ile karışır)
    public void setHesaplananCeza(Double hesaplananCeza) {
        this.hesaplananCeza = hesaplananCeza;
    }
}