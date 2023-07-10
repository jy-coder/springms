package com.springproject.orderservice.repository;

import com.springproject.orderservice.BaseTest;
import com.springproject.orderservice.controller.OrderController;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.entity.OrderLineItems;

import com.springproject.orderservice.mapper.AutoOrderMapper;
import com.springproject.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class OrderRepositoryTest extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;

    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderController = new OrderController(orderService);
    }
    @Test
    public void shouldCreateOrder() {
        OrderDto orderDto = new OrderDto(
                1L,
                "ORD001",
                AutoOrderMapper.MAPPER.mapToOrderLineItemsDtoList(
                        List.of(
                                new OrderLineItems(1L, "SKU001", BigDecimal.valueOf(100), 2),
                                new OrderLineItems(2L, "SKU002", BigDecimal.valueOf(50), 3)
                        )
                )
        );

        when(orderService.createOrder(orderDto)).thenReturn("Order placed successfully");
        CompletableFuture<String> result = orderController.createOrder(orderDto);

        assertNotNull(result);
        try {
            assertEquals("Order placed successfully", result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("An exception occurred while executing the asynchronous task.");
        }
        verify(orderService).createOrder(orderDto);
    }


    @Test
    public void shouldSaveOrder() {
        OrderLineItems lineItem1 = new OrderLineItems(null, "SKU001", BigDecimal.valueOf(100), 2);
        OrderLineItems lineItem2 = new OrderLineItems(null, "SKU002", BigDecimal.valueOf(50), 3);
        List<OrderLineItems> lineItems = new ArrayList<>();
        lineItems.add(lineItem1);
        lineItems.add(lineItem2);

        Order expectedOrderObject = new Order(null, "ORD001", lineItems);

        Order actualOrderObject = orderRepository.save(expectedOrderObject);

        assertThat(actualOrderObject).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedOrderObject);
    }

    @Test
    public void shouldGetAllOrders() {
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
    }
}
