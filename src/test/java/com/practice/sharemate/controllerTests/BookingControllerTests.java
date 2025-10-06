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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private BookingRequest bookingRequest;
    private Booking booking;

    @BeforeEach
    void create() {
        User user = new User();
        Item item = new Item();

        item.setComments(new ArrayList<>());

        LocalDateTime start = LocalDateTime.of(2025, 10, 5, 18, 30, 1);
        LocalDateTime end = start.plusDays(1);
        bookingRequest = new BookingRequest(1L, start, end);

        booking = new Booking();
        booking.setStart(bookingRequest.getStart());
        booking.setEnd(bookingRequest.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Booking.BookingStatus.WAITING);
    }

    @Test
    void addBooking() throws Exception {
        Mockito.when(bookingService.addBooking(Mockito.anyLong(), Mockito.any(BookingRequest.class)))
                .thenReturn(bookingMapper.entityToDto(booking));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": \"1\", \"start\": \"2025-10-05T18:30:01\", \"end\": \"2025-10-06T18:30:01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value("2025-10-05T18:30:01"))
                .andExpect(jsonPath("$.end").value("2025-10-06T18:30:01"));
    }
}
