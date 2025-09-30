package com.practice.sharemate.controller;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.model.Item;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDTO> findAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId, @RequestParam int from, @RequestParam int size) {
        return itemService.findAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDTO findItemById(@PathVariable Long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDTO> findItemByNameOrDescription(@RequestParam String text, @RequestParam int from, @RequestParam int size) {
        return itemService.findItemByNameOrDescription(text, from, size);
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

