package com.isabel.productservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.isabel.productservice.entity.Product;
import com.isabel.productservice.exception.DataIntegrityViolationException;
import com.isabel.productservice.exception.ProductNotFoundException;
import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;
import com.isabel.productservice.repository.ProductRepository;
import com.isabel.productservice.service.ProductService;


@Service // class can now be injected
public class ProductServiceImpl implements ProductService {

    
    private final ProductRepository productRepository;

    // constructor injection
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    @Override
    public ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest) {
        try {
            var savedProduct =  productRepository.save(mapToProductEntity(productCreateRequest));
            return mapToProductCreateResponse(savedProduct);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate product code. Please choose a different product code.");
        }
    }

    // CREATE MULTIPLE PRODUCTS
    @Override
    public List<ProductCreateResponse> createProducts(List<ProductCreateRequest> productCreateRequests) {
        List<ProductCreateResponse> responses = new ArrayList<>();
        for (ProductCreateRequest request : productCreateRequests) {
            try {
                Product savedProduct = productRepository.save(mapToProductEntity(request));
                ProductCreateResponse response = mapToProductCreateResponse(savedProduct);
                responses.add(response);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("Duplicate product code. Please choose a different product code.");
            }
        }
        return responses;
    }
    

    private Product mapToProductEntity(ProductCreateRequest source){
        Product target = new Product();
        System.out.println("target" + target);
        BeanUtils.copyProperties(source, target);
        System.out.println("target after" + target);
        
        return target;

    }
    private ProductCreateResponse mapToProductCreateResponse(Product source){
        ProductCreateResponse target = new ProductCreateResponse();
        BeanUtils.copyProperties(source, target);
        return target;

    }

    @Override
    public List<ProductCreateResponse> findAll(String sortBy) {
        Sort sort = sortBy != null ? Sort.by(sortBy) : Sort.unsorted();
       return productRepository.findAll(sort).stream().map(this::mapToProductCreateResponse).toList(); // data from the db

      }

    @Override
    public ProductCreateResponse findById(Integer productId) {
     var pr =  productRepository.findById(productId);
     
     if(pr.isPresent()){
        return mapToProductCreateResponse(pr.get());
       }
        throw new ProductNotFoundException("Product with id not found");
    }

   

    
}
