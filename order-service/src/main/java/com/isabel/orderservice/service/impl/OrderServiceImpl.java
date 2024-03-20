package com.isabel.orderservice.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.isabel.orderservice.entity.Order;
import com.isabel.orderservice.entity.OrderItem;
import com.isabel.orderservice.model.OrderItemRequest;
import com.isabel.orderservice.model.OrderRequest;
import com.isabel.orderservice.repository.OrderRepository;
import com.isabel.orderservice.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

   private final OrderRepository orderRepository;
   
   public OrderServiceImpl(OrderRepository orderRepository) {
      this.orderRepository = orderRepository;
   }

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderTime(Instant.now());
        var orderItems =  orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
        order.setOrderItems(orderItems);

        orderRepository.save(order);
    }

    private OrderItem mapToOrderItemEntity(OrderItemRequest itemRequest) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(itemRequest, orderItem);
        return orderItem;

    }
    
}
