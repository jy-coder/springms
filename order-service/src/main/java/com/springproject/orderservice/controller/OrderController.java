package com.springproject.orderservice.controller;

import com.springproject.orderservice.dto.OrderRequest;
import com.springproject.orderservice.entity.OrderDto;
import com.springproject.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/orders")
@Slf4j
public class OrderController {
    private OrderService orderService;

    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    public CompletableFuture<String> createOrder(@RequestBody OrderDto order){
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


    public CompletableFuture<String>  fallbackMethod(Throwable throwable) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(()->"Please try again later!");
    }
}
