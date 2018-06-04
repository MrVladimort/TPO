package com.example.tpo.repository;

import com.example.tpo.model.Autor;
import com.example.tpo.model.Pozycje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PozycjeRepository extends JpaRepository<Pozycje, String> {
    @Override
    List<Pozycje> findAll();
    List<Pozycje> findAllByTytul(String tytul);
    List<Pozycje> findAllByTytulAndAutorAndRok(String tytul, Long rok, Autor autor);
    List<Pozycje> findByTytulContainingAndRokAndAutor(String tytul, Long rok, Autor autor);
    List<Pozycje> findByTytulContainingAndAutor(String tytul, Autor autor);

    @Query("SELECT p FROM Pozycje p WHERE UPPER(p.tytul) LIKE upper(CONCAT('%',:tytul,'%')) AND p.autor=:id")
    List<Pozycje> findSuka(@Param("tytul") String username, @Param("id") Autor autor);
}
