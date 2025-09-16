package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking requestToEntity(BookingRequest bookingRequest) {
        return Booking.builder()
                .start(bookingRequest.getStart())
                .end(bookingRequest.getEnd())
                .id(bookingRequest.getItemId())
                .build();
    }

    public BookingDTO entityToDto(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(userMapper.entityToDto(booking.getBooker()))
                .item(itemMapper.entityToDto(booking.getItem()))
                .build();
    }

    public List<BookingDTO> listToDto(List<Booking> bookings) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDTOS.add(entityToDto(booking));
        }
        return bookingDTOS;
    }
}
