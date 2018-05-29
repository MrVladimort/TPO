package com.example.tpo.repository;

import com.example.tpo.model.Wydawca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WydawcaRepository extends JpaRepository<Wydawca, Long> {

}
