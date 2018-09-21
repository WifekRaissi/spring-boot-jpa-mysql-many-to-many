package com.axeane.manyToMany.services;

import com.axeane.manyToMany.model.Projet;
import com.axeane.manyToMany.repositories.ProjetRepository;
import com.axeane.manyToMany.repositories.SalariesRepository;
import com.axeane.manyToMany.utils.ExceptionResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepository;
    private final SalariesRepository salariesRepository;

    public ProjetServiceImpl(ProjetRepository projetRepository, SalariesRepository salariesRepository) {
        this.projetRepository = projetRepository;
        this.salariesRepository = salariesRepository;
    }

    @Override
    public void addProjet(Long salarieId, Projet projet) {
        salariesRepository.findById(salarieId).map(salarie -> {
            projet.getSalaries().add(salarie);
            return projetRepository.save(projet);
        });
    }

    @Override
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public void deleteProjet(Long salarieId, Long projetId) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("projetId" + projetId + " not found");
        }
        projetRepository.findById(projetId).map(projet -> {
            projetRepository.delete(projet);
            return projet;
        });
    }

    @Override
    public void updateProjet(Long salarieId, Projet projetRequest) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("salarieId" + salarieId + " not found");
        }
        projetRepository.findById(projetRequest.getId()).map(projet -> {
            projet.setNom(projetRequest.getNom());
            projet.setDescription(projetRequest.getDescription());
            projet.setDelai((Timestamp) projetRequest.getDelai());
            return projetRepository.save(projet);
        });
    }
}
