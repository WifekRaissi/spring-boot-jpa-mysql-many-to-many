package com.axeane.manyToMany.controllers;

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
