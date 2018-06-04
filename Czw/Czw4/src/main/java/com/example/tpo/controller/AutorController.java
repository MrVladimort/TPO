package com.example.tpo.controller;

import com.example.tpo.model.Autor;
import com.example.tpo.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AutorController {

    @Autowired
    AutorRepository autorRepository;

    @GetMapping("/autor")
    public List<Autor> getAllAutors() {
        return autorRepository.findAll();
    }
    // Create a new Note

    // Get a Single Note

    // Update a Note

    // Delete a Note
}


