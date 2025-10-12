package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.model.Request;
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
public class RequestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void addRequest() throws Exception {
        User user = new User();
        user.setName("Vicky Wolff");
        user.setEmail("Jerrold.Rolfson@gmail.com");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Vicky Wolff"))
                .andExpect(jsonPath("$.email").value("Jerrold.Rolfson@gmail.com"));


        Request request = new Request();
        request.setDescription("Need hammer to break down the wall");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty())
                .andExpect(jsonPath("$.answers").exists());
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].requestorId").value("1"))
                .andExpect(jsonPath("$[0].description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].updated").isEmpty())
                .andExpect(jsonPath("$[0].answers").exists());
    }

    @Test
    @Order(3)
    void findAllPagination() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].requestorId").value("1"))
                .andExpect(jsonPath("$[0].description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].updated").isEmpty())
                .andExpect(jsonPath("$[0].answers").exists());
    }

    @Test
    @Order(4)
    void findUserRequestById() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty())
                .andExpect(jsonPath("$.answers").exists());
    }

    @Test
    @Order(5)
    void updateRequest() throws Exception {
        Request request = new Request();
        request.setDescription("Need another brick in the wall");

        String updateRequestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need another brick in the wall"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists())
                .andExpect(jsonPath("$.answers").exists());
    }

    @Test
    @Order(6)
    void deleteRequest() throws Exception {
        mockMvc.perform(delete("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}