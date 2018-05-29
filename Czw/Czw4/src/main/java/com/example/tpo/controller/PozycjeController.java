package com.example.tpo.controller;


import com.example.tpo.model.Pozycje;
import com.example.tpo.repository.PozycjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PozycjeController {

    @Autowired
    PozycjeRepository pozycjeRepository;

    @GetMapping("/pozycje")
    public List<Pozycje> getAllPozycjes() {
        return pozycjeRepository.findAll();
    }
    // Create a new Note

    // Get a Single Note

    // Update a Note

    // Delete a Note
}
