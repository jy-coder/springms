package com.springproject.inventoryservice.mapper;
import com.springproject.inventoryservice.entity.InventoryDto;
import com.springproject.inventoryservice.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AutoInventoryMapper {
    AutoInventoryMapper MAPPER = Mappers.getMapper(AutoInventoryMapper.class);

    InventoryDto mapToInventoryDto(Inventory inventory);

    Inventory mapToInventory(InventoryDto inventoryDto);
}
