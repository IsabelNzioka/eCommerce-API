package com.isabel.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isabel.orderservice.model.GenericResponse;
import com.isabel.orderservice.model.OrderRequest;
import com.isabel.orderservice.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("api/v1/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("placeOrder")
    public GenericResponse<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        
        GenericResponse<String> resp = GenericResponse.<String>builder()
            .success(true)
            .msg("Order Placed successfully")
            .data(orderService.placeOrder(orderRequest))
            .build();
        return resp;
    }
    
    
    
}
