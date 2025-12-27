package com.example.demo.service;

import com.example.demo.entity.Ceza;
import com.example.demo.repository.CezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CezaService {

    @Autowired
    private CezaRepository cezaRepository;

    // Controller'ın "getAllCezalar" ismini bulabilmesi için bu ismi ekledik
    public List<Ceza> getAllCezalar() {
        return cezaRepository.findAll();
    }

    public List<Ceza> kullaniciCezalari(Integer kullaniciId) {
        return cezaRepository.findByKullaniciId(kullaniciId);
    }

    public List<Ceza> odenmemisCezalar() {
        return cezaRepository.findByOdendiMiFalse();
    }

    @Transactional
    public Ceza cezaOde(Integer cezaId) {
        Ceza ceza = cezaRepository.findById(cezaId)
                .orElseThrow(() -> new RuntimeException("Ceza kaydı bulunamadı."));
        ceza.setOdendiMi(true);
        return cezaRepository.save(ceza);
    }
}