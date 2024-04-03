package com.isabel.productservice.service;

import java.util.List;

import com.isabel.productservice.model.CategoryCreateRequest;
import com.isabel.productservice.model.CategoryCreateResponse;

public interface CategoryService {


    CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryCreateResponse findById(Integer categoryId);
    CategoryCreateResponse findByName(String categoryName);

    List<CategoryCreateResponse> findAll();
    
}
