package com.isabel.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isabel.productservice.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Integer>{
    // DAO
    // access inbuilt springboot methods - ex findAll, findOne etc
    // Product - actual class
    // Integer - key used to identify Product - id

    
}