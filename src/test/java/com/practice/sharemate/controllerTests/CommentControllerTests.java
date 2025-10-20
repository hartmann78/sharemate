package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    private static final User owner = new User();
    private static Long ownerId;

    private static final Item item = new Item();
    private static Long itemId;

    private static final User booker = new User();
    private static Long bookerId;

    private static final BookingRequest bookingRequest = new BookingRequest();
    private static Long bookingId;

    private static final Comment comment = new Comment();
    private static Long commentId;

    @BeforeAll
    static void create() {
        owner.setName("Benjamin Zemlak");
        owner.setEmail("Jesus96@yahoo.com");

        item.setName("Hammer");
        item.setDescription("Pretty useful to break down the wall");
        item.setAvailable(true);

        booker.setName("Derek Mann");
        booker.setEmail("Heidi37@yahoo.com");

        bookingRequest.setStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(6));
        bookingRequest.setEnd(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(7));

        comment.setText("Pretty Good!");
    }

    @Test
    @Order(1)
    void postComment() throws Exception {
        // Create owner
        String ownerJson = objectMapper.writeValueAsString(owner);

        ResultActions ownerResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(owner.getName()))
                .andExpect(jsonPath("$.email").value(owner.getEmail()));

        ownerId = objectMapper.readValue(ownerResult.andReturn().getResponse().getContentAsString(), UserDTO.class).getId();

        // Create item
        String itemJson = objectMapper.writeValueAsString(item);

        ResultActions itemResult = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.comments").exists());

        itemId = objectMapper.readValue(itemResult.andReturn().getResponse().getContentAsString(), ItemDTO.class).getId();

        // Create booker
        String bookerJson = objectMapper.writeValueAsString(booker);

        ResultActions bookerResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(booker.getName()))
                .andExpect(jsonPath("$.email").value(booker.getEmail()));

        bookerId = objectMapper.readValue(bookerResult.andReturn().getResponse().getContentAsString(), UserDTO.class).getId();

        // Create booking
        bookingRequest.setItemId(itemId);

        String bookingRequestJson = objectMapper.writeValueAsString(bookingRequest);

        ResultActions bookingResult = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.start").value(bookingRequest.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingRequest.getEnd().toString()))
                .andExpect(jsonPath("$.booker.id").value(bookerId))
                .andExpect(jsonPath("$.booker.name").value(booker.getName()))
                .andExpect(jsonPath("$.booker.email").value(booker.getEmail()))
                .andExpect(jsonPath("$.item.id").value(itemId))
                .andExpect(jsonPath("$.item.name").value(item.getName()))
                .andExpect(jsonPath("$.item.description").value(item.getDescription()))
                .andExpect(jsonPath("$.item.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.item.comments").exists())
                .andExpect(jsonPath("$.status").value("WAITING"));

        bookingId = objectMapper.readValue(bookingResult.andReturn().getResponse().getContentAsString(), BookingDTO.class).getId();

        // Approve booking
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        // Create commentary
        TimeUnit.SECONDS.sleep(7);

        String commentJson = objectMapper.writeValueAsString(comment);

        ResultActions commentResult = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.authorName").value(booker.getName()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty());

        commentId = objectMapper.readValue(commentResult.andReturn().getResponse().getContentAsString(), CommentDTO.class).getId();
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/items/{itemId}/comment", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentId))
                .andExpect(jsonPath("$[0].text").value(comment.getText()))
                .andExpect(jsonPath("$[0].authorName").value(booker.getName()))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].updated").isEmpty());

    }

    @Test
    @Order(3)
    void findCommentById() throws Exception {
        mockMvc.perform(get("/items/{itemId}/comment/{commentId}", itemId, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.authorName").value(booker.getName()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").isEmpty());

    }

    @Test
    @Order(4)
    void updateComment() throws Exception {
        Comment updateComment = new Comment();
        updateComment.setText("Not Bad!");

        String commentJson = objectMapper.writeValueAsString(updateComment);

        mockMvc.perform(patch("/items/{itemId}/comment/{commentId}", itemId, commentId)
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.text").value(updateComment.getText()))
                .andExpect(jsonPath("$.authorName").value(booker.getName()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.updated").exists());
    }

    @Test
    @Order(5)
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/items/{itemId}/comment/{commentId}", itemId, commentId)
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk());
    }
}
