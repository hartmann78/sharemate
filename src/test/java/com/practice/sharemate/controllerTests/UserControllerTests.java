package com.practice.sharemate.controllerTests;

import com.practice.sharemate.mapper.UserMapper;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @MockitoBean
    private UserService userService;

    private final User user = new User();

    @BeforeEach
    public void create() {
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob1998@gmail.com");
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(userService.findAll())
                .thenReturn(userMapper.listToDto(List.of(user)));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].email").value("bob1998@gmail.com"));
    }

    @Test
    void findById() throws Exception {
        Mockito.when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(userMapper.entityToDto(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob1998@gmail.com"));
    }

    @Test
    void addUser() throws Exception {
        Mockito.when(userService.addUser(Mockito.any(User.class)))
                .thenReturn(userMapper.entityToDto(user));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Bob\", \"email\": \"bob1998@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob1998@gmail.com"));
    }

    @Test
    void updateUser() throws Exception {
        Mockito.when(userService.updateUser(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(userMapper.entityToDto(user));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Bob\", \"email\": \"bob1998@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob1998@gmail.com"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(1L);
    }
}
