package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String gondericiEmail;

    /**
     * Geciken kitaplar için ceza bildirim maili gönderir.
     * Hata almamak için parametre sayısını ve tipini kontrol edin:
     * 1. aliciEmail, 2. adSoyad, 3. kitapAdi, 4. miktar
     */
    public void cezaBildirimiGonder(String aliciEmail, String adSoyad, String kitapAdi, double miktar) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(gondericiEmail);
            message.setTo(aliciEmail);
            message.setSubject("Akıllı Kütüphane - Gecikme Cezası Bilgilendirmesi");
            message.setText("Sayın " + adSoyad + ",\n\n" +
                    "'" + kitapAdi + "' isimli kitabın iade süresi geçtiği için hesabınıza " +
                    String.format("%.2f", miktar) + " TL ceza yansıtılmıştır.\n\n" +
                    "Lütfen en kısa sürede kütüphanemize uğrayınız.\n" +
                    "İyi günler dileriz.");

            mailSender.send(message);
            System.out.println("Ceza bildirimi gönderildi: " + aliciEmail);
        } catch (Exception e) {
            System.err.println("E-posta gönderim hatası (Ceza): " + e.getMessage());
        }
    }

    /**
     * Kitap başarıyla iade edildiğinde teşekkür maili gönderir.
     */
    public void iadeTesekkurMailiGonder(String aliciEmail, String adSoyad, String kitapAdi) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(gondericiEmail);
            message.setTo(aliciEmail);
            message.setSubject("Kitap İadesi Onayı");
            message.setText("Sayın " + adSoyad + ",\n\n" +
                    "'" + kitapAdi + "' isimli kitabı başarıyla iade ettiniz. Teşekkür ederiz.\n\n" +
                    "Keyifli okumalar dileriz.");

            mailSender.send(message);
            System.out.println("İade onayı gönderildi: " + aliciEmail);
        } catch (Exception e) {
            System.err.println("E-posta gönderim hatası (İade): " + e.getMessage());
        }
    }
}