package com.practice.sharemate.controller;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDTO> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAll(userId);
    }

    @GetMapping("/owner")
    public List<BookingDTO> findAllBookingsToOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAllBookingsToOwner(userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDTO findBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @PostMapping
    public BookingDTO addBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingRequest bookingRequest) {
        return bookingService.addBooking(userId, bookingRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDTO patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.patchBooking(userId, bookingId, approved);
    }

    @DeleteMapping
    public void deleteBooking(Long bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}

