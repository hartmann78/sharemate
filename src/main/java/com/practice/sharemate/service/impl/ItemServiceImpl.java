package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.exceptions.*;
import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

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

        if (item.getRequestId() != null && requestRepository.findById(item.getRequestId()).isEmpty()) {
            throw new RequestNotFoundException("Запрос с id " + item.getRequestId() + " не найден!");
        }

        item.setOwnerId(userId);
        item.setComments(new ArrayList<>());
        item.setBookings(new ArrayList<>());

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