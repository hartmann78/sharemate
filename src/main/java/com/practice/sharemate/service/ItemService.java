package com.practice.sharemate.service;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDTO> findAllUserItems(Long userId);

    List<ItemDTO> getItemByNameOrDescription(String text);

    ItemDTO findItemById(Long id);

    ItemDTO addItem(Long userid, Item item);

    ItemDTO updateItem(Long userId, Long itemId, Item item);

    void deleteItem(Long id);
}
