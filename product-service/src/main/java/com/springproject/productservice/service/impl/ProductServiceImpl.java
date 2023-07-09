package com.springproject.productservice.service.impl;


import com.springproject.productservice.entity.ProductDto;
import com.springproject.productservice.entity.Product;
import com.springproject.productservice.exception.ResourceNotFoundException;
import com.springproject.productservice.mapper.AutoProductMapper;
import com.springproject.productservice.repository.ProductRepository;
import com.springproject.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = AutoProductMapper.MAPPER.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        ProductDto savedProductDto = AutoProductMapper.MAPPER.mapToProductDto(savedProduct);
        return savedProductDto;
    }



    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        return AutoProductMapper.MAPPER.mapToProductDto(product);
    }


    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product) -> AutoProductMapper.MAPPER.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {

        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", product.getId())
        );

        existingProduct.setDescription(product.getDescription());
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return AutoProductMapper.MAPPER.mapToProductDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {

        Product existingProduct = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );

        productRepository.deleteById(productId);
    }
}
