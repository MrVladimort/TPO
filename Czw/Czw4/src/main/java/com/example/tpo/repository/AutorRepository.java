package com.example.tpo.repository;

import com.example.tpo.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Override
    List<Autor> findAll();
//    Autor findAutorByName(String name);
    @Query("SELECT a FROM Autor a WHERE UPPER(a.name) LIKE UPPER(CONCAT('%',:username,'%'))")
    List<Autor> findByNameContaining(@Param("username") String username);
//    List<Autor> findByNameContaining(String name);
}
