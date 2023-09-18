package com.springproject.orderservice.controller;

import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.exception.ProductNotInStockException;
import com.springproject.orderservice.service.OrderService;
import com.springproject.utils.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/orders")
@Slf4j
public class OrderController {
    private OrderService orderService;
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    public CompletableFuture<ResponseEntity<String>> createOrder(@RequestBody OrderDto order) {
        log.info("Placing Order");
        return CompletableFuture.supplyAsync(() -> {
                String response = orderService.createOrder(order);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        });
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrder(){
        List<OrderDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }



    public CompletableFuture<String> fallbackMethod(Throwable throwable) {
        log.error("Circuit Breaker Open! Handling fallback", throwable);

        if (throwable instanceof ProductNotInStockException) {
            // Handle "Product is not in stock" scenario
            return CompletableFuture.supplyAsync(() -> "Product is not in stock, please try again later!");
        } else if (throwable instanceof ServiceUnavailableException) {
            return CompletableFuture.supplyAsync(() -> "Service is currently unavailable. Please try again later.");
        } else {
            return CompletableFuture.supplyAsync(() -> "Please try again later!");
        }
    }
}
