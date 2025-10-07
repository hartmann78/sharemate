package com.practice.sharemate.controllerTests;

import com.practice.sharemate.mapper.RequestMapper;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestMapper requestMapper;
    @MockitoBean
    private RequestService requestService;

    private final Request request = new Request();

    @BeforeEach
    void create() {
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob1998@gmail.com");

        request.setId(1L);
        request.setRequestor(user);
        request.setDescription("Need hammer to break down the wall");
        request.setCreated(LocalDateTime.of(2025, 10, 4, 18, 30, 1));
        request.setAnswers(new ArrayList<>());
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(requestService.findAll(Mockito.any()))
                .thenReturn(requestMapper.listToDto(List.of(request)));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].requestorId").value("1"))
                .andExpect(jsonPath("$[0].description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$[0].created").value("2025-10-04T18:30:01"))
                .andExpect(jsonPath("$[0].answers").value(new ArrayList<>()));
    }

    @Test
    void findAllPagination() throws Exception {
        Mockito.when(requestService.findAllPagination(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(requestMapper.listToDto(List.of(request)));

        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].requestorId").value("1"))
                .andExpect(jsonPath("$[0].description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$[0].created").value("2025-10-04T18:30:01"))
                .andExpect(jsonPath("$[0].answers").value(new ArrayList<>()));
    }

    @Test
    void findUserRequestById() throws Exception {
        Mockito.when(requestService.findUserRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestMapper.entityToDto(request));

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$.created").value("2025-10-04T18:30:01"))
                .andExpect(jsonPath("$.answers").value(new ArrayList<>()));
    }

    @Test
    void addRequest() throws Exception {
        Mockito.when(requestService.addRequest(Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(requestMapper.entityToDto(request));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Need hammer to break down the wall\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$.created").value("2025-10-04T18:30:01"))
                .andExpect(jsonPath("$.answers").value(new ArrayList<>()));
    }

    @Test
    void updateRequest() throws Exception {
        Mockito.when(requestService.updateRequest(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(requestMapper.entityToDto(request));

        mockMvc.perform(patch("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requestor.id\": \"1\", " +
                                "\"requestor.name\": \"Bob\", " +
                                "\"requestor.email\": \"bob1998@gmail.com\", " +
                                "\"description\": \"Need hammer to break down the wall\", " +
                                "\"created\": \"2025-10-04T18:30:01\", " +
                                "\"answers\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.requestorId").value("1"))
                .andExpect(jsonPath("$.description").value("Need hammer to break down the wall"))
                .andExpect(jsonPath("$.created").value("2025-10-04T18:30:01"))
                .andExpect(jsonPath("$.answers").value(new ArrayList<>()));
    }

    @Test
    void deleteRequest() throws Exception {
        mockMvc.perform(delete("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        Mockito.verify(requestService, Mockito.times(1))
                .deleteRequest(Mockito.anyLong(), Mockito.anyLong());
    }
}