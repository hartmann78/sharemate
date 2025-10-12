package com.practice.sharemate.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
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

    private static final User owner = new User();
    private static Long ownerId;

    private static final Item item = new Item();
    private static Long itemId;

    private static final User booker = new User();
    private static Long bookerId;

    private static final BookingRequest bookingRequest = new BookingRequest();
    private static Long bookingId;

    @BeforeAll
    static void create() {
        owner.setName("Sheila Kemmer");
        owner.setEmail("Jackeline79@yahoo.com");

        item.setName("Hammer");
        item.setDescription("Pretty useful to break down the wall");
        item.setAvailable(true);

        booker.setName("Claudia Howell");
        booker.setEmail("Floy46@hotmail.com");

        bookingRequest.setStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(2));
    }

    @Test
    @Order(1)
    void addBooking() throws Exception {
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
    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        mockMvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingId))
                .andExpect(jsonPath("$[0].start").value(bookingRequest.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingRequest.getEnd().toString()))
                .andExpect(jsonPath("$[0].booker.id").value(bookerId))
                .andExpect(jsonPath("$[0].booker.name").value(booker.getName()))
                .andExpect(jsonPath("$[0].booker.email").value(booker.getEmail()))
                .andExpect(jsonPath("$[0].item.id").value(itemId))
                .andExpect(jsonPath("$[0].item.name").value(item.getName()))
                .andExpect(jsonPath("$[0].item.description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].item.available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].item.comments").exists())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    @Order(3)
    void findAllBookingsToOwner() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingId))
                .andExpect(jsonPath("$[0].start").value(bookingRequest.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingRequest.getEnd().toString()))
                .andExpect(jsonPath("$[0].booker.id").value(bookerId))
                .andExpect(jsonPath("$[0].booker.name").value(booker.getName()))
                .andExpect(jsonPath("$[0].booker.email").value(booker.getEmail()))
                .andExpect(jsonPath("$[0].item.id").value(itemId))
                .andExpect(jsonPath("$[0].item.name").value(item.getName()))
                .andExpect(jsonPath("$[0].item.description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].item.available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].item.comments").exists())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    @Order(4)
    void findBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
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
    }

    @Test
    @Order(5)
    void patchBooking() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
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
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @Order(6)
    void deleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk());
    }
}
