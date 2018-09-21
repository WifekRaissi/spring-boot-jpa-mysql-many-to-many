package com.axeane.manyToMany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "projet")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);
    @NotEmpty
    @NotNull
    private String nom;
    @NotEmpty
    @NotNull
    private String description;

    private Timestamp delai;
    @ManyToMany(mappedBy = "projets")
    @JsonIgnore
    private Set<Salarie> salaries = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Projet() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDelai() {
        return delai;
    }

    public void setDelai(Timestamp delai) {
        this.delai = delai;
    }

    public Set<Salarie> getSalaries() {
        return salaries;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", delai=" + delai + '}';
    }
}
