package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import generators.BookingGenerator;
import generators.BookingRequestGenerator;
import generators.ItemGenerator;
import generators.UserGenerator;
import repositoryTests.model.Booking;
import repositoryTests.model.BookingRequest;
import repositoryTests.model.Item;
import repositoryTests.model.User;
import repositoryTests.repository.BookingRepository;
import repositoryTests.repository.ItemRepository;
import repositoryTests.repository.UserRepository;

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

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        Item item = itemGenerator.generateItem(owner.getId());
        itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        item.getBookings().add(booking);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(checkOwner.get(), owner);

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(checkBooker.get(), booker);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(checkItem.get(), item);

        Optional<Booking> checkBooking = bookingRepository.findById(booking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(checkBooking.get(), booking);
    }

    @Test
    void getUserBookings() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        Item item = itemGenerator.generateItem(owner.getId());
        itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        item.getBookings().add(booking);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(checkOwner.get(), owner);

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(checkBooker.get(), booker);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(checkItem.get(), item);

        List<Booking> getUserBookings = bookingRepository.findAll();
        assertTrue(getUserBookings.contains(booking));
    }

    @Test
    void getUserBookingById() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        Item item = itemGenerator.generateItem(owner.getId());
        itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        item.getBookings().add(booking);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(checkOwner.get(), owner);

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(checkBooker.get(), booker);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(checkItem.get(), item);

        assertTrue(bookingRepository.checkBookerOrOwnerId(owner.getId()));
        Optional<Booking> getUserBookingById = bookingRepository.findById(booking.getId());

        assertTrue(getUserBookingById.isPresent());
        assertEquals(getUserBookingById.get(), booking);
    }

    @Test
    void approveBooking() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        Item item = itemGenerator.generateItem(owner.getId());
        itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        item.getBookings().add(booking);

        Booking approveBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertEquals(approveBooking.getItem().getOwnerId(), owner.getId());

        approveBooking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(approveBooking);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(checkOwner.get(), owner);

        Optional<User> checkBooker = userRepository.findById(booker.getId());
        assertTrue(checkBooker.isPresent());
        assertEquals(checkBooker.get(), booker);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(checkItem.get(), item);

        Optional<Booking> checkBooking = bookingRepository.findById(approveBooking.getId());
        assertTrue(checkBooking.isPresent());
        assertEquals(checkBooking.get(), approveBooking);
    }
}
