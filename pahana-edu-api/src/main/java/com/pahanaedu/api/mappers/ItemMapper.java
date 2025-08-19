package com.pahanaedu.api.mappers;

import com.pahanaedu.api.dto.ItemDTO;
import com.pahanaedu.models.Item;
import java.util.List;
import java.util.stream.Collectors;

public final class ItemMapper {
    private ItemMapper() {}

    public static ItemDTO toDTO(Item item) {
        if (item == null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setCategory(item.getCategory());
        dto.setAuthor(item.getAuthor());
        dto.setPrice(item.getPrice());
        dto.setStockQuantity(item.getStockQuantity());
        return dto;
    }

    public static Item toModel(ItemDTO dto) {
        if (dto == null) return null;
        Item item = new Item();
        item.setId(dto.getId());
        item.setSku(dto.getSku());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setCategory(dto.getCategory());
        item.setAuthor(dto.getAuthor());
        item.setPrice(dto.getPrice());
        item.setStockQuantity(dto.getStockQuantity());
        return item;
    }

    public static List<ItemDTO> toDTOList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }
}