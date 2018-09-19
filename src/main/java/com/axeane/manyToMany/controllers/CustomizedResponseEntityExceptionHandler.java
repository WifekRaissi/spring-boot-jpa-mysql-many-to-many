package com.axeane.manyToMany.controllers;

import com.axeane.manyToMany.utils.ExceptionResponse;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice()
public class CustomizedResponseEntityExceptionHandler implements ProblemHandling {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponse badRequest(final ExceptionResponse exception, final HttpServletRequest request) {
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(exception.getMessage());
        return error;
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponse notFoundRequest(final ExceptionResponse exception) {
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(exception.getMessage());
        return error;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ExceptionResponse handleException(final Exception exception) {
        ExceptionResponse error = new ExceptionResponse();
        error.setMessage(exception.getMessage());
        return error;
    }
}