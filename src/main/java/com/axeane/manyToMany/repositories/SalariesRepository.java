package com.axeane.manyToMany.repositories;

import com.axeane.manyToMany.model.Salarie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Page<Salarie> findByProjets(Long projetId, Pageable pageable);

    Salarie findSalarieById(Long id);

}
