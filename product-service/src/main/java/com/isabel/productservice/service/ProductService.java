package com.isabel.productservice.service;

import java.util.List;

import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;

public interface ProductService {
  
    ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest);
    List<ProductCreateResponse> createProducts(List<ProductCreateRequest> productCreateRequests);

    List<ProductCreateResponse> findAll(String sortBy);
    
    ProductCreateResponse findById(Integer productId);


    Boolean checkProducts(List<String> productCodes);


}
