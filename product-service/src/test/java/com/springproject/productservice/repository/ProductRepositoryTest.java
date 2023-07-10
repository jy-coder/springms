package com.springproject.productservice.repository;

import com.springproject.productservice.BaseTest;
import com.springproject.productservice.entity.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class ProductRepositoryTest extends BaseTest {
    @Autowired
    private ProductRepository postRepository;

    @Test
    public void shouldSaveProduct() {
        Product expectedProductObject = new Product(null,"Product1", "Product1", BigDecimal.valueOf(1200));
        Product actualProductObject = postRepository.save(expectedProductObject);
        assertThat(actualProductObject).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedProductObject);
    }

}