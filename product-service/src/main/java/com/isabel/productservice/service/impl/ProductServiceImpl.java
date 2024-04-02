package com.isabel.productservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.isabel.productservice.entity.Category;
import com.isabel.productservice.entity.Product;
import com.isabel.productservice.exception.DataIntegrityViolationException;
import com.isabel.productservice.exception.ProductNotFoundException;
import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;
import com.isabel.productservice.repository.CategoryRepository;
import com.isabel.productservice.repository.ProductRepository;
import com.isabel.productservice.service.ProductService;


@Service // class can now be injected
public class ProductServiceImpl implements ProductService {

    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // constructor injection
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

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
                Product newProduct = mapToProductEntity(request);

                Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
                
                newProduct.setCategory(category);

                ProductCreateResponse response = mapToProductCreateResponse(productRepository.save(newProduct));
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

    @Override
    public Boolean checkProducts(List<String> productCodes) {
        List<String> unavailableProducts = new ArrayList<>();

        for (String productCode : productCodes) {
            Product product = productRepository.findByProductCode(productCode);
            if (product == null) {
                unavailableProducts.add(productCode);
            }
        }

        if (unavailableProducts.isEmpty()) {
            return true;
        } else {
            throw new ProductNotFoundException("Product does not exist: " + unavailableProducts);
        }
      
    }

   

    
}
