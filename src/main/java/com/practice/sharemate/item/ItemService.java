package com.practice.sharemate.item;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDTO> findAllItems(Long userId);

    Collection<ItemDTO> getItemByNameOrDescription(String text);

    ItemDTO findItemById(Long id);

    ItemDTO addItem(Long userid, Item item);

    ItemDTO updateItem(Long userId, Long itemId, Item item);

    void deleteItem(Long id);
}
