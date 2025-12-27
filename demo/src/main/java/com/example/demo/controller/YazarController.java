package com.example.demo.controller;

import com.example.demo.entity.Yazar;
import com.example.demo.service.YazarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yazarlar")
@CrossOrigin("*")
public class YazarController {

    @Autowired
    private YazarService yazarService;

    @GetMapping
    public List<Yazar> tumYazarlar() {
        return yazarService.tumYazarlar();
    }

    @PostMapping
    public Yazar yazarEkle(@RequestBody Yazar yazar) {
        return yazarService.yazarEkle(yazar);
    }

    @GetMapping("/ara")
    public List<Yazar> yazarAra(@RequestParam String isim) {
        return yazarService.yazarAra(isim);
    }
}