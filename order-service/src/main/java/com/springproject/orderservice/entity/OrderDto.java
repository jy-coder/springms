package com.springproject.orderservice.entity;

import com.springproject.orderservice.dto.OrderLineItemsDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItemsList;
}
