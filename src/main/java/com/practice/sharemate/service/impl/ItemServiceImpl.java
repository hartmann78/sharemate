package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.exceptions.ForbiddenException;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.repository.UserRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Service
public class ItemServiceImpl implements ItemService {
    private ItemMapper itemMapper;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public List<ItemDTO> findAll(Long userId) {
        List<Item> items;

        if (userId == null) {
            items = itemRepository.findAll();
        } else {
            items = itemRepository.findAllByOwnerId(userId);
        }

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return itemMapper.listToDto(items);
    }

    @Override
    public List<ItemDTO> getItemByNameOrDescription(String text) {
        return itemMapper.listToDto(itemRepository.findAllByNameIgnoreCaseAndAvailableTrueOrDescriptionIgnoreCaseAndAvailableTrue(text, text));
    }

    @Override
    public ItemDTO findItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);

        if (item.isPresent()) {
            return itemMapper.entityToDto(item.get());
        }

        throw new ItemNotFoundException("Предмет с id " + id + " не найден!");
    }

    @Override
    public ItemDTO addItem(Long userId, Item item) {
        if (userId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        item.setOwnerId(userId);
        item.setComments(new ArrayList<>());

        return itemMapper.entityToDto(itemRepository.save(item));
    }

    @Override
    public ItemDTO updateItem(Long userId, Long itemId, Item item) {
        if (userId == null || itemId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Item> checkItem = itemRepository.findById(itemId);

        if (checkItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        Item update = checkItem.get();

        if (!update.getOwnerId().equals(userId)) {
            throw new ForbiddenException("id пользователя не совпадает с id владельца предмета");
        }

        // Обновление данных
        if (item.getName() != null) {
            update.setName(item.getName());
        }

        if (item.getDescription() != null) {
            update.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            update.setAvailable(item.getAvailable());
        }

        return itemMapper.entityToDto(itemRepository.save(update));
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}