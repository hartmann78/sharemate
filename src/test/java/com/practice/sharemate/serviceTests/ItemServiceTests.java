package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.RequestGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.service.RequestService;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
    RequestService requestService;
    @Autowired
    UserGenerator userGenerator;
    @Autowired
    ItemGenerator itemGenerator;
    @Autowired
    RequestGenerator requestGenerator;

    private User user;
    private UserDTO userDTO;

    private Item item;
    private ItemDTO itemDTO;

    @BeforeEach
    void create() {
        user = userGenerator.generateUser();
        userDTO = userService.addUser(user);

        item = itemGenerator.generateItem();
        item.setAvailable(true);
        itemDTO = itemService.addItem(userDTO.getId(), item);
    }

    @Test
    void findAllWithoutUserId() {
        List<ItemDTO> checkItems = itemService.findAll(null, 0, 30);
        assertTrue(checkItems.contains(itemDTO));
    }

    @Test
    void findAllWithUserId() {
        List<ItemDTO> checkItems = itemService.findAll(userDTO.getId(), 0, 1);
        assertTrue(checkItems.contains(itemDTO));
    }

    @Test
    void findItemByNameOrDescription() {
        List<ItemDTO> searchItem = itemService.findItemByNameOrDescription(itemDTO.getName(), 0, 1);
        assertTrue(searchItem.contains(itemDTO));
    }

    @Test
    void findItemById() {
        ItemDTO checkItem = itemService.findItemById(itemDTO.getId());
        assertEquals(itemDTO, checkItem);
    }

    @Test
    void addItem() {
        assertNotNull(itemDTO.getId());
        assertEquals(item.getName(), itemDTO.getName());
        assertEquals(item.getDescription(), itemDTO.getDescription());
        assertEquals(item.getAvailable(), itemDTO.getAvailable());
    }

    @Test
    void addItemOnRequest() {
        User requestor = userGenerator.generateUser();
        UserDTO requestorDTO = userService.addUser(requestor);

        Request request = requestGenerator.generateRequest();
        RequestDTO requestDTO = requestService.addRequest(requestorDTO.getId(), request);

        User owner = userGenerator.generateUser();
        UserDTO ownerDTO = userService.addUser(owner);

        Item itemOnRequest = itemGenerator.generateAvailableItem();
        itemOnRequest.setRequestId(requestDTO.getId());
        ItemDTO itemOnRequestDTO = itemService.addItem(ownerDTO.getId(), itemOnRequest);

        assertNotNull(itemOnRequestDTO.getId());
        assertEquals(itemOnRequest.getName(), itemOnRequestDTO.getName());
        assertEquals(itemOnRequest.getDescription(), itemOnRequestDTO.getDescription());
        assertEquals(itemOnRequest.getAvailable(), itemOnRequestDTO.getAvailable());

        RequestDTO checkRequest = requestService.findUserRequestById(requestorDTO.getId(), requestDTO.getId());
        assertFalse(checkRequest.getAnswers().isEmpty());
    }

    @Test
    void updateItem() {
        Long userId = userDTO.getId();
        Item updateItem = itemGenerator.generateItem();

        ItemDTO updatedItemDTO = itemService.updateItem(userId, itemDTO.getId(), updateItem);
        assertNotNull(updatedItemDTO.getId());
        assertEquals(updateItem.getName(), updatedItemDTO.getName());
        assertEquals(updateItem.getDescription(), updatedItemDTO.getDescription());
        assertEquals(updateItem.getAvailable(), updatedItemDTO.getAvailable());
    }

    @Test
    void deleteItem() {
        itemService.deleteItem(userDTO.getId(), itemDTO.getId());

        assertThrows(ItemNotFoundException.class, () -> itemService.findItemById(itemDTO.getId()));
    }
}
