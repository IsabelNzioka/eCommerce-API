package com.isabel.productservice.model;

import java.math.BigDecimal;

import com.isabel.productservice.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {


  
    private String name;
    private BigDecimal price;
    private String productCode;
    private String image;
    
    private String categoryName;

    
}
