package com.pahanaedu.services;

import com.pahanaedu.dao.IItemDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ItemService implements IItemService {

    private final IItemDAO itemDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    @Override
    public Item addItem(Item item) throws ValidationException {
        validateItemData(item, 0L);
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        return itemDAO.create(item);
    }

    @Override
    public Optional<Item> getItemById(long id) {
        return itemDAO.findById(id);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDAO.findAll();
    }

    @Override
    public Item updateItem(Item item) throws ValidationException {
        Long itemId = item.getId();
        if (itemId == null) {
            throw new ValidationException("Item ID is required for an update.");
        }

        Item existingItem = itemDAO.findById(itemId)
                .orElseThrow(() -> new ValidationException("Item with ID " + itemId + " not found."));

        existingItem.setSku(item.getSku());
        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setCategory(item.getCategory());
        existingItem.setAuthor(item.getAuthor());
        existingItem.setPrice(item.getPrice());
        existingItem.setStockQuantity(item.getStockQuantity());
        existingItem.setActive(item.isActive()); // Make sure to update the active status

        validateItemData(existingItem, existingItem.getId());

        itemDAO.update(existingItem);

        return existingItem;
    }

    @Override
    public void deleteItem(long id) {
        itemDAO.delete(id);
    }

    private void validateItemData(Item item, Long itemIdToIgnore) throws ValidationException {
        if (item.getSku() == null || item.getSku().trim().isEmpty()) {
            throw new ValidationException("SKU (Stock Keeping Unit) cannot be empty.");
        }
        if (itemDAO.existsBySku(item.getSku(), itemIdToIgnore)) {
            throw new ValidationException("An item with this SKU already exists.");
        }

        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new ValidationException("Item Name cannot be empty.");
        }

        if (item.getPrice() == null) {
            throw new ValidationException("Price cannot be empty.");
        }
        if (item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be negative.");
        }

        if (item.getStockQuantity() < 0) {
            throw new ValidationException("Stock Quantity cannot be negative.");
        }
    }
}