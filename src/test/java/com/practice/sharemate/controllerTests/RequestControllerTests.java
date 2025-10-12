package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.Request;
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
public class RequestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final User user = new User();
    private static Long userId;

    private static final Request request = new Request();
    private static Long requestId;

    @BeforeAll
    static void create() {
        user.setName("Vicky Wolff");
        user.setEmail("Jerrold.Rolfson@gmail.com");

        request.setDescription("Need hammer to break down the wall");
    }

    @Test
    @Order(1)
    void addRequest() throws Exception {
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

        // Create request
        String requestJson = objectMapper.writeValueAsString(request);

        ResultActions requestResult = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.requestorId").value(userId))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty())
                .andExpect(jsonPath("$.answers").exists());

        requestId = objectMapper.readValue(requestResult.andReturn().getResponse().getContentAsString(), RequestDTO.class).getId();
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestId))
                .andExpect(jsonPath("$[0].requestorId").value(userId))
                .andExpect(jsonPath("$[0].description").value(request.getDescription()))
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
                .andExpect(jsonPath("$[0].id").value(requestId))
                .andExpect(jsonPath("$[0].requestorId").value(userId))
                .andExpect(jsonPath("$[0].description").value(request.getDescription()))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].updated").isEmpty())
                .andExpect(jsonPath("$[0].answers").exists());
    }

    @Test
    @Order(4)
    void findUserRequestById() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.requestorId").value(userId))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty())
                .andExpect(jsonPath("$.answers").exists());
    }

    @Test
    @Order(5)
    void updateRequest() throws Exception {
        Request updateRequest = new Request();
        updateRequest.setDescription("Need another brick in the wall");

        String updateRequestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.requestorId").value(userId))
                .andExpect(jsonPath("$.description").value(updateRequest.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists())
                .andExpect(jsonPath("$.answers").exists());
    }

    @Test
    @Order(6)
    void deleteRequest() throws Exception {
        mockMvc.perform(delete("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }
}