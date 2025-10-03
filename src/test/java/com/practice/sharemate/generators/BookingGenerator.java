package com.practice.sharemate.generators;

import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;

public class BookingGenerator {
    public Booking generateBooking(BookingRequest bookingRequest, User booker, Item item) {
        return Booking.builder()
                .start(bookingRequest.getStart())
                .end(bookingRequest.getEnd())
                .status(Booking.BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .build();
    }
}
