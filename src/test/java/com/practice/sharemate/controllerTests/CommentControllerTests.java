package com.practice.sharemate.controllerTests;

import com.practice.sharemate.mapper.CommentMapper;
import com.practice.sharemate.model.*;
import com.practice.sharemate.service.CommentService;
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
public class CommentControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentMapper commentMapper;
    @MockitoBean
    private CommentService commentService;

    private final Comment comment = new Comment();

    @BeforeEach
    void create() {
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob1998@gmail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("Hammer");
        item.setDescription("Pretty useful to break down the wall");
        item.setAvailable(false);
        item.setComments(new ArrayList<>());

        LocalDateTime start = LocalDateTime.of(2025, 10, 5, 18, 30, 1);
        LocalDateTime end = start.plusDays(1);
        BookingRequest bookingRequest = new BookingRequest(item.getId(), start, end);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Booking.BookingStatus.APPROVED);

        comment.setId(1L);
        comment.setText("Pretty good!");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(end.plusDays(1));
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(commentService.findAll(Mockito.anyLong()))
                .thenReturn(commentMapper.listToDto(List.of(comment)));

        mockMvc.perform(get("/items/1/comment")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].text").value("Pretty good!"))
                .andExpect(jsonPath("$[0].authorName").value("Bob"))
                .andExpect(jsonPath("$[0].created").value("2025-10-07T18:30:01"));
    }

    @Test
    void findCommentById() throws Exception {
        Mockito.when(commentService.findCommentById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(commentMapper.entityToDto(comment));

        mockMvc.perform(get("/items/1/comment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Pretty good!"))
                .andExpect(jsonPath("$.authorName").value("Bob"))
                .andExpect(jsonPath("$.created").value("2025-10-07T18:30:01"));
    }

    @Test
    void postComment() throws Exception {
        Mockito.when(commentService.postComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(commentMapper.entityToDto(comment));

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Pretty Good!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Pretty good!"))
                .andExpect(jsonPath("$.authorName").value("Bob"))
                .andExpect(jsonPath("$.created").value("2025-10-07T18:30:01"));
    }

    @Test
    void updateComment() throws Exception {
        Mockito.when(commentService.updateComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(commentMapper.entityToDto(comment));

        mockMvc.perform(patch("/items/1/comment/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Pretty Good!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.text").value("Pretty good!"))
                .andExpect(jsonPath("$.authorName").value("Bob"))
                .andExpect(jsonPath("$.created").value("2025-10-07T18:30:01"));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/items/1/comment/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        Mockito.verify(commentService, Mockito.times(1))
                .deleteComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }
}
