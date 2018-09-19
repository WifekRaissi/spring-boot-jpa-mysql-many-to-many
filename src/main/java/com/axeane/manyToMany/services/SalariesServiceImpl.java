package com.axeane.manyToMany.services;

import com.axeane.manyToMany.model.Salarie;
import com.axeane.manyToMany.repositories.SalariesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalariesServiceImpl implements SalariesService {

    private final SalariesRepository salariesRepository;

    public SalariesServiceImpl(SalariesRepository salariesRepository) {
        this.salariesRepository = salariesRepository;
    }

    @Override
    public List<Salarie> findAllSalaries() {
        return salariesRepository.findAll();
    }

    @Override
    public void addSalaries(Salarie salarie) {
        salariesRepository.save(salarie);
    }

    @Override
    public Salarie findSalariedById(Long searchedId) {
        return salariesRepository.findSalarieById(searchedId);
    }

    @Override
    public void deleteSalaried(Long id) {
        Salarie salarie = findSalariedById(id);
        salariesRepository.delete(salarie);
    }

    @Override
    public void updateSalarie(Salarie salarie) {
        Salarie salarie1 = findSalariedById(salarie.getId());
        if (salarie1 != null) {
            salarie1.setNom(salarie.getNom());
            salarie1.setPrenom(salarie.getPrenom());
            salarie1.setAdresse(salarie.getAdresse());
            salarie1.setSalaire(salarie.getSalaire());
        }
    }

}