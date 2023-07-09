package com.springproject.productservice.service;

import com.springproject.productservice.entity.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto product);
    ProductDto getProductById(Long productId);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(ProductDto product);

    void deleteProduct(Long productId);

}
