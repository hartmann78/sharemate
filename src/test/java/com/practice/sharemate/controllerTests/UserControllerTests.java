package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.dto.UserDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final User user = new User();
    private static Long userId;

    @BeforeAll
    static void create() {
        user.setName("Kristy Friesen");
        user.setEmail("Kristy@hotmail.com");
    }

    @Test
    @Order(1)
    void addUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        ResultActions userResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        userId = objectMapper.readValue(userResult.andReturn().getResponse().getContentAsString(), UserDTO.class).getId();
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test
    @Order(3)
    void findById() throws Exception {
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @Order(4)
    void updateUser() throws Exception {
        User updateUser = new User();
        updateUser.setName("Celia Bernhard");
        updateUser.setEmail("Carmella15@hotmail.com");

        String updateUserJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(updateUser.getName()))
                .andExpect(jsonPath("$.email").value(updateUser.getEmail()));
    }

    @Test
    @Order(5)
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}
