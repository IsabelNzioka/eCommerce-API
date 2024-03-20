package com.isabel.productservice.service;

import java.util.List;

import com.isabel.productservice.model.ProductCreateRequest;
import com.isabel.productservice.model.ProductCreateResponse;

public interface ProductService {
  
    ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest);

    List<ProductCreateResponse> findAll();
    ProductCreateResponse findById(Integer productId);


}
