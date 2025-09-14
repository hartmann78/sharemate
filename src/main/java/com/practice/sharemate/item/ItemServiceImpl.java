package com.practice.sharemate.item;

import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.exceptions.ForbiddenException;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private Long globalItemId = 0L;
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDTO> findAllItems(Long userId) {
        Collection<ItemDTO> itemsDTOS = new ArrayList<>();
        for (Item item : itemRepository.findAllItems(userId)) {
            itemsDTOS.add(itemMapper.toDTO(item));
        }
        return itemsDTOS;
    }

    @Override
    public Collection<ItemDTO> getItemByNameOrDescription(String text) {
        Collection<ItemDTO> itemsDTOS = new ArrayList<>();
        for (Item item : itemRepository.getItemByNameOrDescription(text)) {
            itemsDTOS.add(itemMapper.toDTO(item));
        }
        return itemsDTOS;
    }

    @Override
    public ItemDTO findItemById(Long id) {
        Item item = itemRepository.findItemById(id);

        if (item == null) {
            throw new ItemNotFoundException("Предмет с id " + id + " не найден!");
        }

        return itemMapper.toDTO(item);
    }

    @Override
    public ItemDTO addItem(Long userId, Item item) {
        if (userId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (userRepository.findUserById(userId) != null) {
            item.setId(++globalItemId);
            item.setOwnerId(userId);

            return itemMapper.toDTO(itemRepository.addItem(item));
        } else {
            throw new UserNotFoundException("Пользователь с id " + userId + " не существует!");
        }
    }


    @Override
    public ItemDTO updateItem(Long userId, Long itemId, Item item) {
        if (userId == null || itemId == null || item == null) {
            throw new BadRequestException("Неверный запрос");
        }

        Item itemToUpdate = itemRepository.findItemById(itemId);

        if (!itemToUpdate.getOwnerId().equals(userId)) {
            throw new ForbiddenException("id пользователя не совпадает с id владельца предмета");
        }

        if (item.getName() != null) {
            itemToUpdate.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemToUpdate.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemToUpdate.setAvailable(item.getAvailable());
        }

        return itemMapper.toDTO(itemRepository.updateItem(itemToUpdate));
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteItem(id);
    }
}
