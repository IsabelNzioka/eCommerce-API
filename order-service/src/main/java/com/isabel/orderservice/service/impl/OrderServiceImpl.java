package com.isabel.orderservice.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.isabel.orderservice.entity.Order;
import com.isabel.orderservice.entity.OrderItem;
import com.isabel.orderservice.model.GenericResponse;
import com.isabel.orderservice.model.OrderItemRequest;
import com.isabel.orderservice.model.OrderRequest;
import com.isabel.orderservice.repository.OrderRepository;
import com.isabel.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

   private final OrderRepository orderRepository;
   private final WebClient webClient;
   
   public OrderServiceImpl(OrderRepository orderRepository, WebClient webClient) {
      this.orderRepository = orderRepository;
      this.webClient = webClient;
   }

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        // Checks
        // ! All products exits in the inventory
        // http://localhost:6002/api/inventory/create 
        // RestTemplate 

       

        List<String> productCodes = new ArrayList<>();
        List<Integer> productQuantities = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            productCodes.add(orderItemRequest.getProductCode());
            productQuantities.add(orderItemRequest.getQuantity());
        }
        log.info("{}",productCodes);       
        log.info("{}",productQuantities);   
        GenericResponse<Boolean> response = webClient.get()
                .uri("http://localhost:6002/api/inventory/check",
                        uriBuilder -> uriBuilder
                                .queryParam("productCodes", productCodes)
                                .queryParam("productQuantities", productQuantities)
                                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Boolean>>() {
                })
                .block();
        if (response.isSuccess()) {
            // stock
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderTime(Instant.now());
            var orderItems = orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
            order.setOrderItems(orderItems);
            orderRepository.save(order);
        }else{
          // ! throw an exception with the listing of the products that do have enough
          log.error("Not Enough stock");
        }
    }

    private OrderItem mapToOrderItemEntity(OrderItemRequest itemRequest) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(itemRequest, orderItem);
        return orderItem;

    }
    
}
