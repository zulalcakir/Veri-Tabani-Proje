package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Yazarlar")
public class Yazar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer yazar_id;

    private String ad;
    private String soyad;


    public Yazar() {}


    public Integer getYazar_id() { return yazar_id; }
    public void setYazar_id(Integer yazar_id) { this.yazar_id = yazar_id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
}