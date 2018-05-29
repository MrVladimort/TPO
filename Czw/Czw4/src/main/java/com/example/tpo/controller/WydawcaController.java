package com.example.tpo.controller;

//import com.example.tpo.exception.ResourceNotFoundException;

import com.example.tpo.model.Wydawca;
import com.example.tpo.repository.WydawcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WydawcaController {

    @Autowired
    WydawcaRepository wydawcaRepository;

    @GetMapping("/wydawca")
    public List<Wydawca> getAllWydawcas() {
        return wydawcaRepository.findAll();
    }
    // Create a new Note

    // Get a Single Note

    // Update a Note

    // Delete a Note
}
