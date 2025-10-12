package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    @Order(1)
    void addItem() throws Exception {
        User user = new User();
        user.setName("Vernon Cartwright");
        user.setEmail("Marcia49@yahoo.com");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Vernon Cartwright"))
                .andExpect(jsonPath("$.email").value("Marcia49@yahoo.com"));


        Item item = new Item();
        item.setName("Hammer");
        item.setDescription("Pretty useful to break down the wall");
        item.setAvailable(true);

        String itemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Hammer"))
                .andExpect(jsonPath("$.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.comments").exists());
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Hammer"))
                .andExpect(jsonPath("$[0].description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].available").value("true"))
                .andExpect(jsonPath("$[0].comments").exists());
    }

    @Test
    @Order(3)
    void findItemByNameOrDescription() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "useful")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Hammer"))
                .andExpect(jsonPath("$[0].description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].available").value("true"))
                .andExpect(jsonPath("$[0].comments").exists());
    }

    @Test
    @Order(4)
    void findById() throws Exception {
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Hammer"))
                .andExpect(jsonPath("$.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.comments").exists());
    }

    @Test
    @Order(5)
    void updateItem() throws Exception {
        Item item = new Item();
        item.setName("The Wall");
        item.setDescription("Isn't this where we came in?");
        item.setAvailable(false);

        String updateItemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateItemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("The Wall"))
                .andExpect(jsonPath("$.description").value("Isn't this where we came in?"))
                .andExpect(jsonPath("$.available").value("false"))
                .andExpect(jsonPath("$.comments").exists());
    }

    @Test
    @Order(6)
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}
