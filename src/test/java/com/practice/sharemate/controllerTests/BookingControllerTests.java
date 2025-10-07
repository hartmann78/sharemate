package com.practice.sharemate.controllerTests;

import com.practice.sharemate.mapper.BookingMapper;
import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.BookingService;
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
public class BookingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingMapper bookingMapper;
    @MockitoBean
    private BookingService bookingService;

    private Booking booking;

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

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Booking.BookingStatus.WAITING);
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(bookingService.findAll(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingMapper.listToDto(List.of(booking)));

        mockMvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$[0].end").value("2025-10-06T18:30:01"))
                .andExpect(jsonPath("$[0].booker.id").value("1"))
                .andExpect(jsonPath("$[0].booker.name").value("Bob"))
                .andExpect(jsonPath("$[0].booker.email").value("bob1998@gmail.com"))
                .andExpect(jsonPath("$[0].item.id").value("1"))
                .andExpect(jsonPath("$[0].item.name").value("Hammer"))
                .andExpect(jsonPath("$[0].item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].item.available").value("false"))
                .andExpect(jsonPath("$[0].item.comments").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void findAllBookingsToOwner() throws Exception {
        Mockito.when(bookingService.findAllBookingsToOwner(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingMapper.listToDto(List.of(booking)));

        mockMvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$[0].end").value("2025-10-06T18:30:01"))
                .andExpect(jsonPath("$[0].booker.id").value("1"))
                .andExpect(jsonPath("$[0].booker.name").value("Bob"))
                .andExpect(jsonPath("$[0].booker.email").value("bob1998@gmail.com"))
                .andExpect(jsonPath("$[0].item.id").value("1"))
                .andExpect(jsonPath("$[0].item.name").value("Hammer"))
                .andExpect(jsonPath("$[0].item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$[0].item.available").value("false"))
                .andExpect(jsonPath("$[0].item.comments").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void findBookingById() throws Exception {
        Mockito.when(bookingService.findBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingMapper.entityToDto(booking));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$.end").value("2025-10-06T18:30:01"))
                .andExpect(jsonPath("$.booker.id").value("1"))
                .andExpect(jsonPath("$.booker.name").value("Bob"))
                .andExpect(jsonPath("$.booker.email").value("bob1998@gmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("false"))
                .andExpect(jsonPath("$.item.comments").value(new ArrayList<>()))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void addBooking() throws Exception {
        Mockito.when(bookingService.addBooking(Mockito.anyLong(), Mockito.any(BookingRequest.class)))
                .thenReturn(bookingMapper.entityToDto(booking));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": \"1\", " +
                                "\"start\": \"2025-10-05T18:30:01\", " +
                                "\"end\": \"2025-10-06T18:30:01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$.end").value("2025-10-06T18:30:01"))
                .andExpect(jsonPath("$.booker.id").value("1"))
                .andExpect(jsonPath("$.booker.name").value("Bob"))
                .andExpect(jsonPath("$.booker.email").value("bob1998@gmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("false"))
                .andExpect(jsonPath("$.item.comments").value(new ArrayList<>()))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void patchBooking() throws Exception {
        booking.setStatus(Booking.BookingStatus.APPROVED);

        Mockito.when(bookingService.patchBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingMapper.entityToDto(booking));

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$.end").value("2025-10-06T18:30:01"))
                .andExpect(jsonPath("$.booker.id").value("1"))
                .andExpect(jsonPath("$.booker.name").value("Bob"))
                .andExpect(jsonPath("$.booker.email").value("bob1998@gmail.com"))
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.item.name").value("Hammer"))
                .andExpect(jsonPath("$.item.description").value("Pretty useful to break down the wall"))
                .andExpect(jsonPath("$.item.available").value("false"))
                .andExpect(jsonPath("$.item.comments").value(new ArrayList<>()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void deleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1))
                .deleteBooking(Mockito.anyLong(), Mockito.anyLong());
    }
}
