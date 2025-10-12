package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void postComment() throws Exception {
        // Create owner
        User owner = new User();
        owner.setName("Benjamin Zemlak");
        owner.setEmail("Jesus96@yahoo.com");

        String ownerJson = objectMapper.writeValueAsString(owner);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Benjamin Zemlak"))
                .andExpect(jsonPath("$.email").value("Jesus96@yahoo.com"));

        // Create item
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

        // Create booker
        User booker = new User();
        booker.setName("Derek Mann");
        booker.setEmail("Heidi37@yahoo.com");

        String bookerJson = objectMapper.writeValueAsString(booker);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Derek Mann"))
                .andExpect(jsonPath("$.email").value("Heidi37@yahoo.com"));

        // Create booking
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        bookingRequest.setStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1));
        bookingRequest.setEnd(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(2));

        String bookingRequestJson = objectMapper.writeValueAsString(bookingRequest);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").value(bookingRequest.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingRequest.getEnd().toString()))
                .andExpect(jsonPath("$.booker.id").value("2"))
                .andExpect(jsonPath("$.booker.name").value("Derek Mann"))
                .andExpect(jsonPath("$.booker.email").value("Heidi37@yahoo.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("true"))
                .andExpect(jsonPath("$.item.comments").exists())
                .andExpect(jsonPath("$.status").value("WAITING"));

        // Approve booking
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        // Create commentary
        TimeUnit.SECONDS.sleep(2);

        Comment comment = new Comment();
        comment.setText("Pretty Good!");

        String commentJson = objectMapper.writeValueAsString(comment);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Pretty Good!"))
                .andExpect(jsonPath("$.authorName").value("Derek Mann"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty());
    }


    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/items/1/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].text").value("Pretty Good!"))
                .andExpect(jsonPath("$[0].authorName").value("Derek Mann"))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].updated").isEmpty());

    }

    @Test
    @Order(3)
    void findCommentById() throws Exception {
        mockMvc.perform(get("/items/1/comment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Pretty Good!"))
                .andExpect(jsonPath("$.authorName").value("Derek Mann"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty());

    }

    @Test
    @Order(4)
    void updateComment() throws Exception {
        Comment comment = new Comment();
        comment.setText("Not Bad!");

        String commentJson = objectMapper.writeValueAsString(comment);

        mockMvc.perform(patch("/items/1/comment/1")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Not Bad!"))
                .andExpect(jsonPath("$.authorName").value("Derek Mann"))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists());
    }

    @Test
    @Order(5)
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/items/1/comment/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk());
    }
}
