package com.practice.sharemate.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDTO> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO findItemById(@PathVariable Long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDTO> getItemByNameOrDescription(@RequestParam String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.getItemByNameOrDescription(text);
    }

    @PostMapping
    public ItemDTO addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody Item item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody Item item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}

