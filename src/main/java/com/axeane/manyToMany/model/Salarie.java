package com.axeane.manyToMany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "salarie")
public class Salarie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);

    @NotEmpty
    @NotNull
    private String nom;

    @NotEmpty
    @NotNull
    private String prenom;
    private BigDecimal salaire;

    @NotEmpty
    @NotNull
    @Size(max = 256, message = "address should have maximum 256 characters")
    private String adresse;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Salarie_Projet",
            joinColumns = {@JoinColumn(name = "salarie_id")},
            inverseJoinColumns = {@JoinColumn(name = "projet_id")}
    )
    @JsonIgnore
    private
    Set<Projet> projets = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getAdresse() {
        return adresse;
    }

    @Required
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }

    public Salarie(@NotEmpty @NotNull String nom, @NotEmpty @NotNull String prenom, @NotEmpty @NotNull @Size(max = 256, message = "address should have maximum 256 characters") String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }

    public Salarie() {
    }

    @Override
    public String toString() {
        return "Salarie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                ", adresse='" + adresse + '\'' + '}';
    }
}
