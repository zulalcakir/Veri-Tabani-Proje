package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "cezalar")
@Data
public class Ceza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ceza_id") // Önceki hatayı önlemek için eşledik
    private Integer id;

    @Column(name = "ceza_miktari")
    private BigDecimal miktar; // Numeric/Decimal hatasını önlemek için BigDecimal yaptık

    @Column(name = "odendi_mi")
    private Boolean odendiMi = false;

    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;
}