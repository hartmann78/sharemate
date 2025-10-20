package com.practice.sharemate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.sharemate.model.Booking;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    private Booking.BookingStatus status;
    private UserDTO booker;
    private ItemDTO item;
}
