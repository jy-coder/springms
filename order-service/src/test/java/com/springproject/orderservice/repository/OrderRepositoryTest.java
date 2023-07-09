package com.springproject.orderservice.repository;

import com.springproject.orderservice.BaseTest;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderLineItems;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest extends BaseTest {
    @Autowired
    private OrderRepository orderRepository;

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
}
