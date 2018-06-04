package com.example.tpo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "POZYCJE")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Pozycje implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ISBN")
    private String id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "AUTID")
    private Autor autor;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "WYDID")
    private Wydawca wydawca;

    @NotBlank
    @Column(name = "TYTUL")
    private String tytul;

    @NotBlank
    @Column(name = "ROK")
    private Long rok;

    @NotBlank
    @Column(name = "CENA")
    private Double cena;

    public String getId() {
        return id;
    }

    public Autor getAutor() {
        return autor;
    }

    public Wydawca getWydawca() {
        return wydawca;
    }
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public void setRok(Long rok) {
        this.rok = rok;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getRok() {
        return rok;
    }

    public Double getCena() {
        return cena;
    }
}