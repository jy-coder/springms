package com.springproject.inventoryservice.service.impl;


import com.springproject.inventoryservice.dto.InventoryResponse;
import com.springproject.inventoryservice.entity.InventoryDto;
import com.springproject.inventoryservice.entity.Inventory;
import com.springproject.inventoryservice.exception.ResourceNotFoundException;
import com.springproject.inventoryservice.mapper.AutoInventoryMapper;
import com.springproject.inventoryservice.repository.InventoryRepository;
import com.springproject.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private InventoryRepository inventoryRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Inventory inventory = AutoInventoryMapper.MAPPER.mapToInventory(inventoryDto);
        Inventory savedInventory = inventoryRepository.save(inventory);
        InventoryDto savedInventoryDto = AutoInventoryMapper.MAPPER.mapToInventoryDto(savedInventory);
        return savedInventoryDto;
    }



    @Override
    public InventoryDto getInventoryById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory", "id", inventoryId)
        );
        return AutoInventoryMapper.MAPPER.mapToInventoryDto(inventory);
    }


    @Override
    public List<InventoryDto> getAllInventorys() {
        List<Inventory> inventorys = inventoryRepository.findAll();
        return inventorys.stream().map((inventory) -> AutoInventoryMapper.MAPPER.mapToInventoryDto(inventory))
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDto updateInventory(InventoryDto inventory) {

        Inventory existingInventory = inventoryRepository.findById(inventory.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Inventory", "id", inventory.getId())
        );

        existingInventory.setQuantity(inventory.getQuantity());
        existingInventory.setSkuCode(inventory.getSkuCode());
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return AutoInventoryMapper.MAPPER.mapToInventoryDto(updatedInventory);
    }

    @Override
    public void deleteInventory(Long inventoryId) {

        Inventory existingInventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory", "id", inventoryId)
        );

        inventoryRepository.deleteById(inventoryId);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
