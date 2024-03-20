package com.isabel.orderservice.model;

import java.util.List;

import com.isabel.orderservice.entity.OrderItem;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {


    private String productCode;
    private Integer quantity;
    
}
