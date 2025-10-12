package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final User user = new User();
    private static Long userId;

    private static final Item item = new Item();
    private static Long itemId;

    @BeforeAll
    static void create() {
        user.setName("Vernon Cartwright");
        user.setEmail("Marcia49@yahoo.com");

        item.setName("Brick");
        item.setDescription("Part of the wall");
        item.setAvailable(true);
    }

    @Test
    @Order(1)
    void addItem() throws Exception {
        // Create user
        String userJson = objectMapper.writeValueAsString(user);

        ResultActions userResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        userId = objectMapper.readValue(userResult.andReturn().getResponse().getContentAsString(), UserDTO.class).getId();

        // Create item
        String itemJson = objectMapper.writeValueAsString(item);

        ResultActions itemResult = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.comments").exists());

        itemId = objectMapper.readValue(itemResult.andReturn().getResponse().getContentAsString(), ItemDTO.class).getId();
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemId))
                .andExpect(jsonPath("$[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].comments").exists());
    }

    @Test
    @Order(3)
    void findItemByNameOrDescription() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", item.getName())
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemId))
                .andExpect(jsonPath("$[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].comments").exists());
    }

    @Test
    @Order(4)
    void findById() throws Exception {
        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.comments").exists());
    }

    @Test
    @Order(5)
    void updateItem() throws Exception {
        Item updateItem = new Item();
        updateItem.setName("The Wall");
        updateItem.setDescription("Isn't this where we came in?");
        updateItem.setAvailable(false);

        String updateItemJson = objectMapper.writeValueAsString(updateItem);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateItemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value(updateItem.getName()))
                .andExpect(jsonPath("$.description").value(updateItem.getDescription()))
                .andExpect(jsonPath("$.available").value(updateItem.getAvailable()))
                .andExpect(jsonPath("$.comments").exists());
    }

    @Test
    @Order(6)
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }
}
