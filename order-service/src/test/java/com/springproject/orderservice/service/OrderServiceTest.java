package com.springproject.orderservice.service;

import com.springproject.orderservice.aop.InventoryServiceObserver;
import com.springproject.orderservice.dto.OrderLineItemsDto;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.entity.OrderLineItems;

import com.springproject.orderservice.event.OrderPlacedEvent;
import com.springproject.orderservice.mapper.AutoOrderMapper;
import com.springproject.orderservice.repository.OrderRepository;
import com.springproject.orderservice.service.impl.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;
    private OrderDto orderDto;
    private List<OrderLineItemsDto> orderLineItemsDtoList;

    private List<OrderLineItems> orderLineItemsList;

    @Spy
    private WebClient.Builder webClientBuilder = WebClient.builder();

    @Mock
    private  KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Mock
    private InventoryServiceObserver inventoryServiceObserver;

    private Order order;


    @BeforeEach
    public void setup() {
        orderService = new OrderServiceImpl(orderRepository, webClientBuilder, "http://localhost:8085",kafkaTemplate,inventoryServiceObserver);
        orderLineItemsDtoList = List.of(
                new OrderLineItemsDto(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItemsDto(2L, "SKU002", BigDecimal.valueOf(50), 3)
        );

        orderLineItemsList = List.of(
                new OrderLineItems(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItems(2L, "SKU002", BigDecimal.valueOf(50), 3)
        );


        orderDto = OrderDto.builder().id(1L).orderNumber("123").orderLineItemsList(List.of(
                new OrderLineItemsDto(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItemsDto(2L, "SKU002", BigDecimal.valueOf(50), 3)
        )).build();

        order = Order.builder().id(1L).orderNumber("123").orderLineItemsList(List.of(
                new OrderLineItems(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItems(2L, "SKU002", BigDecimal.valueOf(50), 3)
        )).build();
    }


    @Test
    public void AddOrder_AddNewOrder(){
        String response = orderService.createOrder(orderDto);
        assertEquals("Order Placed Successfully!", response);
    }


    @Test
    public void GetAllOrder_ReturnOrdersList(){
       Order order1 = Order.builder().id(2L).orderNumber("123").orderLineItemsList(orderLineItemsList).build();

       given(orderRepository.findAll()).willReturn(List.of(order,order1));

       List<OrderDto> orderList = orderService.getAllOrders();

        Assertions.assertThat(orderList).isNotNull();
        Assertions.assertThat(orderList.size()).isEqualTo(2);
    }


    @Test
    public void UpdateOrder_ReturnUpdatedOrder(){
        long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);
        OrderDto updatedOrder = orderService.updateOrder(orderDto);
        Assertions.assertThat(updatedOrder).isNotNull();
    }

    @Test
    public void DeleteOrderById_ReturnNothing(){
        long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        assertAll(() -> orderService.deleteOrder(orderId));
    }

}
