package com.isabel.inventory.exception;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.isabel.inventory.model.GenericResponse;

@RestControllerAdvice
public class ControllerAdvice {
    
    @ExceptionHandler(NotEnoughQuantityException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public GenericResponse<Map<String, Integer>> handleProductNotFound(NotEnoughQuantityException ex){
            GenericResponse<Map<String, Integer>> resp = GenericResponse.<Map<String, Integer>>builder()
                .success(false)
                .msg(ex.getMessage())
                .data(ex.getUnavailableItems())
                .build();
                return resp;
    }

    @ExceptionHandler(UniqueProductCodeException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public GenericResponse<?> handleDataIntegrityViolation(UniqueProductCodeException ex){
            GenericResponse<?> resp = GenericResponse.builder()
                .success(false)
                .msg(ex.getMessage())
                .build();
                return resp;
    }
}
