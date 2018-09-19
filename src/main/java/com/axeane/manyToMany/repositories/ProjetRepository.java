package com.axeane.manyToMany.repositories;

import com.axeane.manyToMany.model.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    Page<Projet> findBySalariesAfter(Long salarieId, Pageable pageable);

}
