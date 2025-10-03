package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.exceptions.*;
import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.model.Answer;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.repository.AnswerRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final AnswerRepository answerRepository;

    @Override
    public List<ItemDTO> findAll(Long userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неправильный запрос");
        }

        List<Item> items;

        if (userId == null) {
            items = itemRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            items = itemRepository.findAllByOwnerIdPagination(userId, from, size);
        }

        if (items.isEmpty()) {
            throw new ItemNotFoundException("Предметы не найдены");
        }

        return itemMapper.listToDto(items);
    }

    @Override
    public List<ItemDTO> findItemByNameOrDescription(String text, int from, int size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неправильный запрос");
        }

        return itemMapper.listToDto(itemRepository.searchPagination(text, from, size));
    }

    @Override
    public ItemDTO findItemById(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        return itemMapper.entityToDto(item.get());
    }

    @Override
    public ItemDTO addItem(Long userId, Item item) {
        if (userId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        item.setOwnerId(userId);
        item.setBookings(new ArrayList<>());
        item.setComments(new ArrayList<>());
        item.setAnswers(new ArrayList<>());

        if (item.getRequestId() != null) {
            Optional<Request> findRequest = requestRepository.findById(item.getRequestId());
            if (findRequest.isEmpty()) {
                throw new RequestNotFoundException("Запрос с id " + item.getRequestId() + " не найден!");
            } else {
                Answer answer = new Answer(null, item, findRequest.get());
                answerRepository.save(answer);

                item.getAnswers().add(answer);
            }
        }

        itemRepository.save(item);

        return itemMapper.entityToDto(item);
    }

    @Override
    public ItemDTO updateItem(Long userId, Long itemId, Item item) {
        if (userId == null || itemId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (!userRepository.existsById(userId)) {
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