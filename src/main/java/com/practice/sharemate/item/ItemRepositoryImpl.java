package com.practice.sharemate.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> findAllItems(Long userId) {
        if (userId == null) {
            return items.values();
        }

        return items.values()
                .stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .toList();
    }


    @Override
    public Collection<Item> getItemByNameOrDescription(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    @Override
    public Item findItemById(Long id) {
        return items.get(id);
    }

    @Override
    public Item addItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.replace(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }
}
