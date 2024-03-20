package com.isabel.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isabel.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
}
