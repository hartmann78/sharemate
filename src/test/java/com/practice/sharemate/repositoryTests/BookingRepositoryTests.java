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
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingRepository bookingRepository;

    UserGenerator userGenerator = new UserGenerator();
    ItemGenerator itemGenerator = new ItemGenerator();
    BookingRequestGenerator bookingRequestGenerator = new BookingRequestGenerator();
    BookingGenerator bookingGenerator = new BookingGenerator();

    @Test
    void bookItem() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        userRepository.save(booker);

        item.getBookings().add(booking);
        itemRepository.save(item);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(owner, checkOwner.get());

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(booker, checkBooker.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        Optional<Booking> checkBooking = bookingRepository.findById(booking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(booking, checkBooking.get());
    }

    @Test
    void getUserBookings() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        userRepository.save(booker);

        item.getBookings().add(booking);
        itemRepository.save(item);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(owner, checkOwner.get());

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(booker, checkBooker.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        List<Booking> getUserBookings = bookingRepository.findAll();
        assertTrue(getUserBookings.contains(booking));
    }

    @Test
    void getUserBookingById() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        userRepository.save(booker);

        item.getBookings().add(booking);
        itemRepository.save(item);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(owner, checkOwner.get());

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(booker, checkBooker.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        Optional<Booking> checkUserBookingById = bookingRepository.findById(booking.getId());
        assertTrue(checkUserBookingById.isPresent());
        assertEquals(booking, checkUserBookingById.get());
    }

    @Test
    void approveBooking() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        userRepository.save(booker);

        item.getBookings().add(booking);
        itemRepository.save(item);

        Booking approveBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertEquals(approveBooking.getItem().getOwnerId(), owner.getId());

        approveBooking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(approveBooking);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(owner, checkOwner.get());

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(booker, checkBooker.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        Optional<Booking> checkBooking = bookingRepository.findById(approveBooking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(approveBooking, checkBooking.get());
    }

    @Test
    void findAllBookingsToOwnerPagination() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker1 = userGenerator.generateUser();
        userRepository.save(booker1);

        User booker2 = userGenerator.generateUser();
        userRepository.save(booker2);

        User booker3 = userGenerator.generateUser();
        userRepository.save(booker3);

        BookingRequest bookingRequest1 = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking1 = bookingGenerator.generateBooking(bookingRequest1, booker1, item);
        bookingRepository.save(booking1);

        booker1.getBookings().add(booking1);
        userRepository.save(booker1);

        BookingRequest bookingRequest2 = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking2 = bookingGenerator.generateBooking(bookingRequest2, booker2, item);
        bookingRepository.save(booking2);

        booker2.getBookings().add(booking2);
        userRepository.save(booker2);

        BookingRequest bookingRequest3 = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking3 = bookingGenerator.generateBooking(bookingRequest3, booker3, item);
        bookingRepository.save(booking3);

        booker1.getBookings().add(booking3);
        userRepository.save(booker3);

        item.getBookings().add(booking1);
        item.getBookings().add(booking2);
        item.getBookings().add(booking3);
        itemRepository.save(item);

        List<Booking> bookingsToOwner = bookingRepository.findAllBookingsToOwnerPagination(owner.getId(), 1, 1);
        assertEquals(1, bookingsToOwner.size());
        assertTrue(bookingsToOwner.contains(booking2));
    }

    @Test
    void findAllByBookerIdPagination() {
        User owner1 = userGenerator.generateUser();
        userRepository.save(owner1);

        Item item1 = itemGenerator.generateAvailableItemWithOwnerId(owner1.getId());
        itemRepository.save(item1);

        User owner2 = userGenerator.generateUser();
        userRepository.save(owner2);

        Item item2 = itemGenerator.generateAvailableItemWithOwnerId(owner2.getId());
        itemRepository.save(item2);

        User owner3 = userGenerator.generateUser();
        userRepository.save(owner3);

        Item item3 = itemGenerator.generateAvailableItemWithOwnerId(owner3.getId());
        itemRepository.save(item3);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest1 = bookingRequestGenerator.generateBookingRequest(item1.getId());
        Booking booking1 = bookingGenerator.generateBooking(bookingRequest1, booker, item1);
        bookingRepository.save(booking1);

        BookingRequest bookingRequest2 = bookingRequestGenerator.generateBookingRequest(item2.getId());
        Booking booking2 = bookingGenerator.generateBooking(bookingRequest2, booker, item2);
        bookingRepository.save(booking2);

        BookingRequest bookingRequest3 = bookingRequestGenerator.generateBookingRequest(item3.getId());
        Booking booking3 = bookingGenerator.generateBooking(bookingRequest3, booker, item3);
        bookingRepository.save(booking3);

        List<Booking> bookingsByBooker = bookingRepository.findAllByBookerIdPagination(booker.getId(), 1, 1);
        assertEquals(1, bookingsByBooker.size());
        assertTrue(bookingsByBooker.contains(booking2));
    }

    @Test
    void findBookingByBookerIdAndItemId() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item item = itemGenerator.generateAvailableItemWithOwnerId(owner.getId());
        itemRepository.save(item);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        assertEquals(booking, bookingRepository.findBookingByBookerIdAndItemId(booker.getId(), item.getId()));
    }
}
