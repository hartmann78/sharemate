package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.model.BookingRequest;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void addBooking() throws Exception {
        // Create owner
        User owner = new User();
        owner.setName("Sheila Kemmer");
        owner.setEmail("Jackeline79@yahoo.com");

        String ownerJson = objectMapper.writeValueAsString(owner);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sheila Kemmer"))
                .andExpect(jsonPath("$.email").value("Jackeline79@yahoo.com"));

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
        booker.setName("Claudia Howell");
        booker.setEmail("Floy46@hotmail.com");

        String bookerJson = objectMapper.writeValueAsString(booker);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Claudia Howell"))
                .andExpect(jsonPath("$.email").value("Floy46@hotmail.com"));

        // Create booking
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        bookingRequest.setStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(2));

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
                .andExpect(jsonPath("$.booker.name").value("Claudia Howell"))
                .andExpect(jsonPath("$.booker.email").value("Floy46@hotmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("true"))
                .andExpect(jsonPath("$.item.comments").exists())
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].booker.id").value("2"))
                .andExpect(jsonPath("$[0].booker.name").value("Claudia Howell"))
                .andExpect(jsonPath("$[0].booker.email").value("Floy46@hotmail.com"))
                .andExpect(jsonPath("$[0].item.id").value("1"))
                .andExpect(jsonPath("$[0].item.name").value("Hammer"))
                .andExpect(jsonPath("$[0].item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].item.available").value("true"))
                .andExpect(jsonPath("$[0].item.comments").exists())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    @Order(3)
    void findAllBookingsToOwner() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].booker.id").value("2"))
                .andExpect(jsonPath("$[0].booker.name").value("Claudia Howell"))
                .andExpect(jsonPath("$[0].booker.email").value("Floy46@hotmail.com"))
                .andExpect(jsonPath("$[0].item.id").value("1"))
                .andExpect(jsonPath("$[0].item.name").value("Hammer"))
                .andExpect(jsonPath("$[0].item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].item.available").value("true"))
                .andExpect(jsonPath("$[0].item.comments").exists())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    @Order(4)
    void findBookingById() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.booker.id").value("2"))
                .andExpect(jsonPath("$.booker.name").value("Claudia Howell"))
                .andExpect(jsonPath("$.booker.email").value("Floy46@hotmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("true"))
                .andExpect(jsonPath("$.item.comments").exists())
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    @Order(5)
    void patchBooking() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.booker.id").value("2"))
                .andExpect(jsonPath("$.booker.name").value("Claudia Howell"))
                .andExpect(jsonPath("$.booker.email").value("Floy46@hotmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("true"))
                .andExpect(jsonPath("$.item.comments").exists())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @Order(6)
    void deleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk());
    }
}
