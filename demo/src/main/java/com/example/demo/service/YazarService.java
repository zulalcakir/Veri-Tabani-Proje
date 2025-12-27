package com.example.demo.service;

import com.example.demo.entity.Yazar;
import com.example.demo.repository.YazarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class YazarService {

    @Autowired
    private YazarRepository yazarRepository;

    public Yazar yazarEkle(Yazar yazar) {
        return yazarRepository.save(yazar);
    }

    public List<Yazar> tumYazarlar() {
        return yazarRepository.findAll();
    }

    public List<Yazar> yazarAra(String isim) {
        return yazarRepository.findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(isim, isim);
    }

    public Yazar yazarBul(Integer id) {
        return yazarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yazar bulunamadı!"));
    }
}