package com.axeane.manyToMany.services;

import com.axeane.manyToMany.model.Salarie;

import java.util.List;

public interface SalariesService {

    List<Salarie> findAllSalaries();

    void addSalaries(Salarie salarie);

    Salarie findSalariedById(Long searchedId);

    void deleteSalaried(Long id);

    void updateSalarie(Salarie salarie);

}