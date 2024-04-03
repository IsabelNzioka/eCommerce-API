package com.isabel.productservice.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.isabel.productservice.entity.Category;
import com.isabel.productservice.exception.DataIntegrityViolationException;
import com.isabel.productservice.exception.ProductNotFoundException;
import com.isabel.productservice.model.CategoryCreateRequest;
import com.isabel.productservice.model.CategoryCreateResponse;
import com.isabel.productservice.repository.CategoryRepository;
import com.isabel.productservice.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        try {
            var savedCategory =  categoryRepository.save(mapToCategoryEntity(categoryCreateRequest));
            return mapToCategoryCreateResponse(savedCategory);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate Category Name");
        }
    }

    private CategoryCreateResponse mapToCategoryCreateResponse(Category savedCategory) {
        CategoryCreateResponse target = new CategoryCreateResponse();
        BeanUtils.copyProperties(savedCategory, target);
        return target;
    }

    private Category mapToCategoryEntity(CategoryCreateRequest categoryCreateRequest) {
         Category target = new Category();
         BeanUtils.copyProperties(categoryCreateRequest, target);
         return target;
    }

    @Override
    public CategoryCreateResponse findById(Integer categoryId) {
          var category = categoryRepository.findById(categoryId);
           if(category.isPresent()){
             return mapToCategoryCreateResponse(category.get());
       }
        throw new ProductNotFoundException("Category with id not found");
    }

    @Override
    public CategoryCreateResponse findByName(String categoryName) {
       var category = categoryRepository.findByName(categoryName);
       if(category.isPresent()){
        return mapToCategoryCreateResponse(category.get());
      }
            throw new ProductNotFoundException("Category with Name not found");
    }

    @Override
    public List<CategoryCreateResponse> findAll() {
        return categoryRepository.findAll().stream().map(this::mapToCategoryCreateResponse).toList();
  }

  
}
