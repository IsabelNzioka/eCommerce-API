package com.isabel.orderservice.service;

import com.isabel.orderservice.model.OrderRequest;
import com.isabel.orderservice.repository.OrderRepository;

public interface OrderService{

    String placeOrder(OrderRequest orderRequest);
    
}
