package com.isabel.productservice.model;

import java.util.List;

import com.isabel.productservice.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCreateResponse {


    // private Integer id;
    private String name;
    private List<Product> products;
    
}
