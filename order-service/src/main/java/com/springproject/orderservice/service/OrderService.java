package com.springproject.orderservice.service;

import com.springproject.orderservice.entity.OrderDto;

import java.util.List;

public interface OrderService {
    String createOrder(OrderDto order,int userId);
    OrderDto getOrderById(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrder(OrderDto order);

    void deleteOrder(Long orderId);

}
