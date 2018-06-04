package com.example.tpo.controller;

import com.example.tpo.model.Autor;
import com.example.tpo.subclasses.Filter;
import com.example.tpo.model.Pozycje;
import com.example.tpo.repository.PozycjeRepository;
import com.example.tpo.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PozycjeController {

    @Autowired
    PozycjeRepository pozycjeRepository;
    @Autowired
    AutorRepository autorRepository;

    @GetMapping("/pozycje")
    public List<Pozycje> getAllPozycjes() {
        return pozycjeRepository.findAll();
    }

    @PostMapping("/pozycje/filtered")
    public List<Pozycje> getFilteredPozycjes(@Valid @RequestBody Filter filter) {
        List<Pozycje> lp = new ArrayList<>();
        for (Autor a : filter.getAutor() != null ? autorRepository.findByNameContaining(filter.getAutor()) : autorRepository.findAll()) {
            if (filter.getRok() != null) {
                List<Pozycje> tmp = pozycjeRepository.findByTytulContainingAndRokAndAutor(filter.getTytul() != null ? filter.getTytul() : "",
                        Long.parseLong(filter.getRok()), a);
                lp.addAll(tmp);
            } else {
                List<Pozycje> tmp = pozycjeRepository.findSuka(filter.getTytul() != null ? filter.getTytul() : "", a);
                lp.addAll(tmp);
            }
        }

        return lp;
    }

    // Create a new Note

    // Get a Single Note

    // Update a Note

    // Delete a Note
}
