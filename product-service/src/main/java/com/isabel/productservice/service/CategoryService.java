package com.isabel.productservice.service;

import com.isabel.productservice.model.CategoryCreateRequest;
import com.isabel.productservice.model.CategoryCreateResponse;

public interface CategoryService {


    CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryCreateResponse findById(Integer categoryId);
    
}
