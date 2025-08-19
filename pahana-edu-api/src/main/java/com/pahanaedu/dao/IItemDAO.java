package com.pahanaedu.dao;

import com.pahanaedu.models.Item;
import java.util.List;
import java.util.Optional;

public interface IItemDAO {
    Item create(Item item);
    Optional<Item> findById(long id);
    List<Item> findAll();
    void update(Item item);
    void delete(long id);
    boolean existsBySku(String sku, Long itemIdToIgnore);
}