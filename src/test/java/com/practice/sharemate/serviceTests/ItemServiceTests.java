package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ItemServiceTests {
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    UserGenerator userGenerator;
    @Autowired
    ItemGenerator itemGenerator;

    @Test
    void findAllWithoutUserId() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        List<ItemDTO> checkItems = itemService.findAll(null, 0, 20);
        assertTrue(checkItems.contains(itemDTO));
    }

    @Test
    void findAllWithUserId() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        List<ItemDTO> checkItems = itemService.findAll(userDTO.getId(), 0, 1);
        assertTrue(checkItems.contains(itemDTO));
    }

    @Test
    void findItemByNameOrDescription() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateAvailableItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        List<ItemDTO> searchItem = itemService.findItemByNameOrDescription(itemDTO.getName(), 0, 1);
        assertTrue(searchItem.contains(itemDTO));
    }

    @Test
    void findItemById() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateAvailableItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        ItemDTO checkItem = itemService.findItemById(itemDTO.getId());
        assertEquals(itemDTO, checkItem);
    }

    @Test
    void addItem() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        assertNotNull(itemDTO.getId());
        assertEquals(item.getName(), itemDTO.getName());
        assertEquals(item.getDescription(), itemDTO.getDescription());
        assertEquals(item.getAvailable(), itemDTO.getAvailable());
    }

    @Test
    void updateItem() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        Long userId = userDTO.getId();
        Item updateItem = itemGenerator.generateItemWithoutOwnerId();

        ItemDTO updatedItemDTO = itemService.updateItem(userId, itemDTO.getId(), updateItem);
        assertNotNull(updatedItemDTO.getId());
        assertEquals(updateItem.getName(), updatedItemDTO.getName());
        assertEquals(updateItem.getDescription(), updatedItemDTO.getDescription());
        assertEquals(updateItem.getAvailable(), updatedItemDTO.getAvailable());
    }

    @Test
    void deleteItem() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Item item = itemGenerator.generateItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(userDTO.getId(), item);

        itemService.deleteItem(userDTO.getId(), itemDTO.getId());

        assertThrows(ItemNotFoundException.class, () -> itemService.findItemById(itemDTO.getId()));
    }
}
