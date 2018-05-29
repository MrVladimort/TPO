package com.example.tpo.repository;

import com.example.tpo.model.Pozycje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PozycjeRepository extends JpaRepository<Pozycje, String> {

}
