package com.axeane.manyToMany.controllers;


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