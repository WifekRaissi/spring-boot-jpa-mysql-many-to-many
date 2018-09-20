# spring-boot-jpa-mysql-many-to-many

Dans les deux tutoriaux précédants on a étudié le mapping dans les relation One to Many et One to One :

https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-many

https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-one

On finit dans le présent tutorial par la relation Many to Many entre la table Salarie et Projet. Un salarié peut intégrer plusieurs projets et un projet peut être affecté à plusieurs salariés.
L'architecture du projet est la suivante:

![alt text](https://github.com/WifekRaissi/spring-boot-jpa-mysql-many-to-many/blob/master/src/main/resources/architecture.PNG)       

##  Projet.java

```
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

    /*public void setSalaries(Set<Salarie> salaries) {
        this.salaries = salaries;
    }*/

    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", delai=" + delai +
                '}';
    }
}

```


##    Salarie.java

```

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

    @NotNull
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

    public Salarie() {
    }

    @Override
    public String toString() {
        return "Salarie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + salaire.hashCode();
        result = 31 * result + adresse.hashCode();
        return result;
    }
}


```
##  Repository

##    ProjetRepository.java

```

import com.axeane.manyToMany.model.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    Page<Projet> findBySalariesAfter(Long salarieId, Pageable pageable);

}

```

##   SalariesRepository.java

```

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

```
##   Service

##   ProjetServiceImpl.java

```

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

```


##   SalariesServiceImpl


```

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

```

##  Controllers
##   ProjetController.java

```

import com.axeane.manyToMany.model.Projet;
import com.axeane.manyToMany.services.ProjetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }


    @ApiOperation(value = "add a new task")
    @PostMapping("/salaries/{salarieId}/projets")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created task")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createProjet(@PathVariable(value = "salarieId") Long salarieId,
                                       @Valid @RequestBody Projet projet) {
        projetService.addProjet(salarieId, projet);
        return new ResponseEntity<>(projet, HttpStatus.CREATED);
    }


    @ApiOperation(value = " list projects", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping("/salaries/{salarieId}/projets")
    public ResponseEntity getProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }

    @ApiOperation(value = "update a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated project"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping("/salaries/{salarieId}/projets/")
    public ResponseEntity updateProjet(@PathVariable(value = "salarieId") Long salarieId,
                                       @Valid @RequestBody Projet projetRequest) {
        projetService.updateProjet(salarieId, projetRequest);
        return new ResponseEntity<>(projetRequest, HttpStatus.OK);
    }

    @ApiOperation(value = "delete a project")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted project"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })

    @DeleteMapping("/salaries/{salarieId}/projets/{projetId}")
    public ResponseEntity deleteSalarie(@PathVariable(value = "salarieId") Long salarieId,
                                        @PathVariable(value = "projetId") Long projetId) {
        projetService.deleteProjet(salarieId, projetId);
        return new ResponseEntity(HttpStatus.OK);
    }

}

```



##  SalariesController

```

import com.axeane.manyToMany.model.Salarie;
import com.axeane.manyToMany.services.SalariesService;
import com.axeane.manyToMany.utils.ExceptionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/salaries")
@Api(value = "gestion des salariés", description = "Operations pour la gestion des salariés")
public class SalariesController {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }

    private Logger logger = LoggerFactory.getLogger(SalariesController.class);
    private final SalariesService salariesService;

    public SalariesController(SalariesService salariesService) {
        this.salariesService = salariesService;
    }


    @ApiOperation(value = "add a new salaried")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created salaried")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addSalaries(@Valid @RequestBody Salarie salarie) {
        salariesService.addSalaries(salarie);
        return new ResponseEntity<>(salarie, HttpStatus.CREATED);
    }


    @ApiOperation(value = "View a list of available salaries", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/salaries")
    public ResponseEntity getAllSalaries() {
        List<Salarie> salaries = salariesService.findAllSalaries();
        return new ResponseEntity<>(salaries, HttpStatus.OK);
    }


    @ApiOperation(value = "find a salaried by its id", response = Salarie.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity getSalariesById(@PathVariable("id") long id) {
        Salarie salarie = salariesService.findSalariedById(id);
        if (salarie != null) {
            logger.info("Salaried:" + salarie);
            return new ResponseEntity<>(salarie, HttpStatus.OK);
        }
        throw new ExceptionResponse();
    }

    @ApiOperation(value = "update a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping
    public ResponseEntity updateSalaries(@RequestBody Salarie salarie) {
        if (salariesService.findSalariedById(salarie.getId()) != null) {
            logger.info("Salaried:" + salarie);
            salariesService.updateSalarie(salarie);
            return new ResponseEntity<>(salarie, HttpStatus.OK);
        }
        throw new ExceptionResponse();
    }

    @ApiOperation(value = "delete a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSalaries(@PathVariable("id") Long id) {
        Salarie salarie = salariesService.findSalariedById(id);
        if (salarie != null) {
            salariesService.deleteSalaried(id);
            logger.info("Deleted:");
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
```

