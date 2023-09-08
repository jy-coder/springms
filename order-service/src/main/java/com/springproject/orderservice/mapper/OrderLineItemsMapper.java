package com.springproject.orderservice.mapper;

import com.springproject.orderservice.entity.OrderLineItems;
import com.springproject.orderservice.dto.OrderLineItemsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper

public interface OrderLineItemsMapper {
    OrderLineItemsMapper INSTANCE = Mappers.getMapper(OrderLineItemsMapper.class);
    OrderLineItemsDto mapToOrderLineItemsDto(OrderLineItems orderLineItems);
    OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto);
}
