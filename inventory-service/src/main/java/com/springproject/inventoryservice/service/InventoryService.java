package com.springproject.inventoryservice.service;

import com.springproject.inventoryservice.entity.InventoryDto;
import com.springproject.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryDto createInventory(InventoryDto inventory);
    InventoryDto getInventoryById(Long inventoryId);

    List<InventoryDto> getAllInventorys();

    InventoryDto updateInventory(InventoryDto inventory);

    void deleteInventory(Long inventoryId);
    List<InventoryResponse> isInStock(List<String> skuCode);

}
