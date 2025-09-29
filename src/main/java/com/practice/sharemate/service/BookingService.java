package com.practice.sharemate.service;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.model.BookingRequest;

import java.util.List;

public interface BookingService {
    List<BookingDTO> findAllUserBookings(Long userId);

    List<BookingDTO> findAllBookingsToOwner(Long userId);

    BookingDTO findBookingById(Long userId,Long bookingId);

    BookingDTO addBooking(Long userId, BookingRequest bookingRequest);

    BookingDTO patchBooking(Long userId, Long bookingId, Boolean approved);

    void deleteBooking(Long bookingId);
}
