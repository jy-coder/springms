package com.springproject.productservice.mapper;
import com.springproject.productservice.entity.ProductDto;
import com.springproject.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AutoProductMapper {
    AutoProductMapper MAPPER = Mappers.getMapper(AutoProductMapper.class);

    ProductDto mapToProductDto(Product product);

    Product mapToProduct(ProductDto productDto);
}
