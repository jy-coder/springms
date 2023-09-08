package com.springproject.orderservice.controller;

import com.springproject.orderservice.dto.OrderRequest;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.exception.ProductNotInStockException;
import com.springproject.orderservice.service.OrderService;
import com.springproject.utils.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;

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
    public CompletableFuture<String> createOrder(@RequestBody OrderDto order) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader.substring(7);
        String username = jwtUtil.getTokenInfo(accessToken);
//        OrderDto savedOrder = orderService.createOrder(order);
//        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
//        return "Order placed successfully";
        log.info("Placing Order");
        return CompletableFuture.supplyAsync(()-> orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrder(){
        List<OrderDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }



    public CompletableFuture<String> fallbackMethod(Throwable throwable) {
        log.error("Circuit Breaker Open! Handling fallback", throwable);

        // Check the type of the exception and customize the fallback behavior
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
