package com.springproject.orderservice.service.impl;


import com.springproject.orderservice.aop.InventoryServiceObserver;
import com.springproject.orderservice.entity.InventoryResponse;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderLineItems;
import com.springproject.orderservice.event.OrderPlacedEvent;
import com.springproject.orderservice.exception.ProductNotInStockException;
import com.springproject.orderservice.exception.ResourceNotFoundException;
import com.springproject.orderservice.mapper.AutoOrderMapper;
import com.springproject.orderservice.repository.OrderRepository;
import com.springproject.orderservice.service.OrderService;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private final WebClient  webClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    @Autowired
    private InventoryServiceObserver inventoryServiceObserver;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,WebClient.Builder webClientBuilder, @Value("${inventory.service.base-url}") String baseUrl,  KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createOrder(OrderDto orderDto) {
        Order order = AutoOrderMapper.MAPPER.mapToOrder(orderDto);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        inventoryServiceObserver.observeInventoryServiceCall(() -> {
            InventoryResponse[] inventoryResponseArray = webClient.get()
                    .uri("/api/v1/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order Placed Successfully!";
            } else {
                throw new ProductNotInStockException();
            }
        });
        return "Order Placed Successfully!";

    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );
        return AutoOrderMapper.MAPPER.mapToOrderDto(order);
    }


    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map((order) -> AutoOrderMapper.MAPPER.mapToOrderDto(order))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrder(OrderDto order) {

        Order existingOrder = orderRepository.findById(order.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", order.getId())
        );

        existingOrder.setOrderNumber(order.getOrderNumber());
        existingOrder.setOrderLineItemsList(AutoOrderMapper.MAPPER.mapToOrderLineItemsList(order.getOrderLineItemsList()));
        Order updatedOrder = orderRepository.save(existingOrder);
        return AutoOrderMapper.MAPPER.mapToOrderDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {

        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId)
        );

        orderRepository.deleteById(orderId);
    }
}
