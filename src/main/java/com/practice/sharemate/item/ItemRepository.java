package com.practice.sharemate.item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findAllItems(Long userId);

    Collection<Item> getItemByNameOrDescription(String text);

    Item findItemById(Long id);

    Item addItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long id);
}
