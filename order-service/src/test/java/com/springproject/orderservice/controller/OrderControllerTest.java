package com.springproject.orderservice.controller;


import com.springproject.orderservice.dto.OrderLineItemsDto;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.entity.OrderLineItems;
import com.springproject.orderservice.service.OrderService;

import com.springproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    private OrderDto orderDto;
    private List<OrderLineItemsDto> orderLineItemsDtoList;

    private List<OrderLineItems> orderLineItemsList;
    private Order order;

    private int userId;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @BeforeEach
    public void init() {
        userId = 1;
        orderLineItemsDtoList = List.of(
                new OrderLineItemsDto(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItemsDto(2L, "SKU002", BigDecimal.valueOf(50), 3)
        );

        orderLineItemsList = List.of(
                new OrderLineItems(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItems(2L, "SKU002", BigDecimal.valueOf(50), 3)
        );


        order = Order.builder().orderNumber("123").orderLineItemsList(List.of(
                new OrderLineItems(1L, "SKU001", BigDecimal.valueOf(100), 2),
                new OrderLineItems(2L, "SKU002", BigDecimal.valueOf(50), 3)
        )).build();

        orderDto = OrderDto.builder()
                .orderNumber("ORD-003")
                .orderLineItemsList(List.of(
                        new OrderLineItemsDto(null, "iphone_13", BigDecimal.valueOf(10.99), 2),
                        new OrderLineItemsDto(null, "iphone_13", BigDecimal.valueOf(19.99), 3)
                ))
                .build();

    }


    @Test
    public void CreateOrder_ReturnCreated() throws  Exception {
        int userId = 1;
        when(orderService.createOrder(orderDto,userId)).thenReturn("");
        when(jwtUtil.getUserId(any())).thenReturn(userId);

        MvcResult mvcResult   = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
               .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated());

    }


    @Test
    public void GetAllOrders_ReturnOrderListDto() throws Exception {

        OrderDto newOrderDto = OrderDto.builder()
                .orderNumber("ORD-004")
                .orderLineItemsList(List.of(
                        new OrderLineItemsDto(null, "iphone_13", BigDecimal.valueOf(10.99), 2),
                        new OrderLineItemsDto(null, "iphone_13", BigDecimal.valueOf(19.99), 3)
                ))
                .build();


        List<OrderDto> orderDtoList = List.of(
                orderDto,
                newOrderDto
        );
        when(orderService.getAllOrders()).thenReturn(orderDtoList);

        ResultActions response = mockMvc.perform(get("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

}
