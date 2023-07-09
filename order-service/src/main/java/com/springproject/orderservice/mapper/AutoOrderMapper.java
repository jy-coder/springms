package com.springproject.orderservice.mapper;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.entity.Order;
import com.springproject.orderservice.entity.OrderLineItems;
import com.springproject.orderservice.entity.OrderLineItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface AutoOrderMapper {
    AutoOrderMapper MAPPER = Mappers.getMapper(AutoOrderMapper.class);

    OrderDto mapToOrderDto(Order order);

    Order mapToOrder(OrderDto orderDto);

    default List<OrderLineItemsDto> mapToOrderLineItemsDtoList(List<OrderLineItems> orderLineItemsList) {
        return orderLineItemsList.stream()
                .map(OrderLineItemsMapper.INSTANCE::mapToOrderLineItemsDto)
                .collect(Collectors.toList());
    }

    default List<OrderLineItems> mapToOrderLineItemsList(List<OrderLineItemsDto> orderLineItemsDtoList) {
        return orderLineItemsDtoList.stream()
                .map(OrderLineItemsMapper.INSTANCE::mapToOrderLineItems)
                .collect(Collectors.toList());
    }
}
