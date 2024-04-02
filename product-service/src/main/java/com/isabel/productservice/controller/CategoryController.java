package com.isabel.productservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.isabel.productservice.model.CategoryCreateRequest;
import com.isabel.productservice.model.CategoryCreateResponse;
import com.isabel.productservice.model.GenericResponse;
import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;
import com.isabel.productservice.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RequestMapping("api/categories")
@RestController
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getMethodName(@RequestParam(required=false) String param) {
        return "Hello World";
    }
    
    @GetMapping("/{categoryId}")
    public GenericResponse<CategoryCreateResponse> findById(@PathVariable Integer categoryId) {
        CategoryCreateResponse pr = categoryService.findById(categoryId);
       GenericResponse<CategoryCreateResponse> resp = GenericResponse.<CategoryCreateResponse>builder()
                .success(true)
                .msg("Data fetched Successfully")
                .data(pr)
                .build();
                
                log.info("We returned : {}",pr);
                return resp;
    }


    @PostMapping
    public GenericResponse<CategoryCreateResponse> createCategory(@RequestBody CategoryCreateRequest categoryCreateRequest) {
                log.info("We received : {}",categoryCreateRequest);
                CategoryCreateResponse pr = categoryService.createCategory(categoryCreateRequest);

        GenericResponse<CategoryCreateResponse> resp = GenericResponse.<CategoryCreateResponse>builder()
                .success(true)
                .msg("Data saved Successfully")
                .data(pr)
                .build();
                log.info("We returned : {}",pr);
        return resp;
    }
    
}
