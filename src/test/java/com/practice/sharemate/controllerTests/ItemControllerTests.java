package com.practice.sharemate.controllerTests;

import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemMapper itemMapper;
    @MockitoBean
    private ItemService itemService;

    private final Item item = new Item();

    @BeforeEach
    void create() {
        item.setId(1L);
        item.setName("Hammer");
        item.setDescription("Pretty useful to break down the wall");
        item.setAvailable(false);
        item.setComments(new ArrayList<>());
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(itemService.findAll(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemMapper.listToDto(List.of(item)));

        mockMvc.perform(get("/items?from=0&size=1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Hammer"))
                .andExpect(jsonPath("$[0].description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].available").value("false"));
    }

    @Test
    void findItemByNameOrDescription() throws Exception {
        Mockito.when(itemService.findItemByNameOrDescription(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemMapper.listToDto(List.of(item)));

        mockMvc.perform(get("/items/search?text=hammer&from=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Hammer"))
                .andExpect(jsonPath("$[0].description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].available").value("false"));
    }

    @Test
    void findById() throws Exception {
        Mockito.when(itemService.findItemById(Mockito.anyLong()))
                .thenReturn(itemMapper.entityToDto(item));

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Hammer"))
                .andExpect(jsonPath("$.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.available").value("false"));
    }

    @Test
    void addItem() throws Exception {
        Mockito.when(itemService.addItem(Mockito.anyLong(), Mockito.any(Item.class)))
                .thenReturn(itemMapper.entityToDto(item));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Hammer\", \"description\": \"Pretty useful to break down the wall\", \"available\": \"false\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Hammer"))
                .andExpect(jsonPath("$.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.available").value("false"));
    }

    @Test
    void updateItem() throws Exception {
        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Item.class)))
                .thenReturn(itemMapper.entityToDto(item));

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Hammer\", \"description\": \"Pretty useful to break down the wall\", \"available\": \"false\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Hammer"))
                .andExpect(jsonPath("$.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.available").value("false"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        Mockito.verify(itemService, Mockito.times(1))
                .deleteItem(1L, 1L);
    }
}
