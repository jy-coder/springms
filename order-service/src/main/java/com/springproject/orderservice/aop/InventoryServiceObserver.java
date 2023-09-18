package com.springproject.orderservice.aop;

import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Component;


import java.util.function.Supplier;

@Component
    @Observed(
            name = "inventory-service-lookup",
            contextualName = "inventory-service-lookup",
            lowCardinalityKeyValues = {"call", "inventory-service"})
public class InventoryServiceObserver {
    public boolean observeInventoryServiceCall(Supplier<Boolean> action) {
        return action.get();
    }
}