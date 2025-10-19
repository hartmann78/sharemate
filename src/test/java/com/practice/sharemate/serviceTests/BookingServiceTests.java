package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.BookingNotFoundException;
import com.practice.sharemate.generators.BookingRequestGenerator;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.BookingService;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BookingServiceTests {
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserGenerator userGenerator;
    @Autowired
    ItemGenerator itemGenerator;
    @Autowired
    BookingRequestGenerator bookingRequestGenerator;

    private User owner;
    private Item item;
    private User booker;
    private BookingRequest bookingRequest;

    private UserDTO ownerDTO;
    private ItemDTO itemDTO;
    private UserDTO bookerDTO;
    private BookingDTO bookingDTO;

    @BeforeEach
    void create() {
        owner = userGenerator.generateUser();
        ownerDTO = userService.addUser(owner);

        item = itemGenerator.generateAvailableItem();
        itemDTO = itemService.addItem(ownerDTO.getId(), item);

        booker = userGenerator.generateUser();
        bookerDTO = userService.addUser(booker);

        bookingRequest = bookingRequestGenerator.generateBookingRequest(itemDTO.getId());
        bookingDTO = bookingService.addBooking(bookerDTO.getId(), bookingRequest);
    }

    @Test
    void findAllWithoutUserId() {
        List<BookingDTO> checkBookings = bookingService.findAll(null, 0, 10);
        assertTrue(checkBookings.contains(bookingDTO));
    }

    @Test
    void findAllWithUserId() {
        List<BookingDTO> checkBookingsByBooker = bookingService.findAll(bookerDTO.getId(), 0, 10);
        assertTrue(checkBookingsByBooker.contains(bookingDTO));
    }

    @Test
    void findAllBookingsToOwner() {
        List<BookingDTO> checkBookingsToOwner = bookingService.findAllBookingsToOwner(ownerDTO.getId(), 0, 10);
        assertTrue(checkBookingsToOwner.contains(bookingDTO));
    }

    @Test
    void findBookingById() {
        BookingDTO checkBooking = bookingService.findBookingById(bookerDTO.getId(), bookingDTO.getId());
        assertEquals(bookingDTO, checkBooking);
    }

    @Test
    void addBooking() {
        assertNotNull(bookingDTO.getId());
        assertEquals(bookingRequest.getStart(), bookingDTO.getStart());
        assertEquals(bookingRequest.getEnd(), bookingDTO.getEnd());
        assertEquals(Booking.BookingStatus.WAITING, bookingDTO.getStatus());
        assertEquals(bookerDTO, bookingDTO.getBooker());
        assertEquals(itemDTO, bookingDTO.getItem());
    }

    @Test
    void patchBookingApproved() {
        BookingDTO approvedBooking = bookingService.patchBooking(ownerDTO.getId(), bookingDTO.getId(), true);

        itemDTO.setAvailable(false);

        assertNotNull(approvedBooking.getId());
        assertEquals(bookingRequest.getStart(), approvedBooking.getStart());
        assertEquals(bookingRequest.getEnd(), approvedBooking.getEnd());
        assertEquals(bookerDTO, approvedBooking.getBooker());
        assertEquals(itemDTO, approvedBooking.getItem());

        assertEquals(Booking.BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void patchBookingRejected() {
        BookingDTO approvedBooking = bookingService.patchBooking(ownerDTO.getId(), bookingDTO.getId(), false);

        assertNotNull(approvedBooking.getId());
        assertEquals(bookingRequest.getStart(), approvedBooking.getStart());
        assertEquals(bookingRequest.getEnd(), approvedBooking.getEnd());
        assertEquals(bookerDTO, approvedBooking.getBooker());
        assertEquals(itemDTO, approvedBooking.getItem());

        assertEquals(Booking.BookingStatus.REJECTED, approvedBooking.getStatus());
    }

    @Test
    void deleteBooking() {
        bookingService.deleteBooking(bookerDTO.getId(), bookingDTO.getId());

        assertThrows(BookingNotFoundException.class, () -> bookingService.findBookingById(bookerDTO.getId(), bookingDTO.getId()));
    }
}
