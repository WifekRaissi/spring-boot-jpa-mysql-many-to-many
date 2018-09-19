package com.axeane.manyToMany.services;

import com.axeane.manyToMany.model.Projet;

import java.util.List;

public interface ProjetService {

    void addProjet(Long salarieId, Projet projet);

    List<Projet> getAllProjets();

    void deleteProjet(Long salarieId, Long projetId);

    void updateProjet(Long salarieId, Projet projetRequest);

}
