package com.pahanaedu.services;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Item;
import java.util.List;
import java.util.Optional;

public interface IItemService {
    Item addItem(Item item) throws ValidationException;
    Optional<Item> getItemById(long id);
    List<Item> getAllItems();
    Item updateItem(Item item) throws ValidationException;
    void deleteItem(long id);
}