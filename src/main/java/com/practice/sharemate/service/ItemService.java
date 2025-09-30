package com.practice.sharemate.service;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDTO> findAll(Long userId, int from, int size);

    List<ItemDTO> findItemByNameOrDescription(String text, int from, int size);

    ItemDTO findItemById(Long id);

    ItemDTO addItem(Long userid, Item item);

    ItemDTO updateItem(Long userId, Long itemId, Item item);

    void deleteItem(Long id);
}
