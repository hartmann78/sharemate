package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.BookingRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.practice.sharemate.generators.BookingGenerator;
import com.practice.sharemate.generators.BookingRequestGenerator;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.UserGenerator;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookingRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final UserGenerator userGenerator = new UserGenerator();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final BookingRequestGenerator bookingRequestGenerator = new BookingRequestGenerator();
    private final BookingGenerator bookingGenerator = new BookingGenerator();

    private User owner;
    private Item item;
    private User booker;
    private BookingRequest bookingRequest;
    private Booking booking;

    @BeforeEach
    void create() {
        owner = userGenerator.generateUser();
        userRepository.save(owner);

        item = itemGenerator.generateAvailableItemWithOwner(owner);
        itemRepository.save(item);

        booker = userGenerator.generateUser();
        userRepository.save(booker);

        bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        userRepository.save(booker);

        item.getBookings().add(booking);
        itemRepository.save(item);
    }

    @Test
    void bookItem() {
        Optional<Booking> checkBooking = bookingRepository.findById(booking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(booking, checkBooking.get());
    }

    @Test
    void getUserBookings() {
        List<Booking> getUserBookings = bookingRepository.findAll();
        assertTrue(getUserBookings.contains(booking));
    }

    @Test
    void getUserBookingById() {
        Optional<Booking> checkUserBookingById = bookingRepository.findById(booking.getId());
        assertTrue(checkUserBookingById.isPresent());
        assertEquals(booking, checkUserBookingById.get());
    }

    @Test
    void approveBooking() {
        Booking approveBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertEquals(approveBooking.getItem().getOwner().getId(), owner.getId());

        approveBooking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(approveBooking);

        Optional<Booking> checkBooking = bookingRepository.findById(approveBooking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(approveBooking, checkBooking.get());
        assertEquals(Booking.BookingStatus.APPROVED, approveBooking.getStatus());
    }

    @Test
    void findAllBookingsToOwnerPagination() {
        List<Booking> bookingsToOwner = bookingRepository.findAllBookingsToOwnerPagination(owner.getId(), 0, 1);
        assertTrue(bookingsToOwner.contains(booking));
    }

    @Test
    void findAllByBookerIdPagination() {
        List<Booking> bookingsByBooker = bookingRepository.findAllByBookerIdPagination(booker.getId(), 0, 1);
        assertTrue(bookingsByBooker.contains(booking));
    }

    @Test
    void findBookingByBookerIdAndItemId() {
        assertEquals(booking, bookingRepository.findBookingByBookerIdAndItemId(booker.getId(), item.getId()));
    }
}
