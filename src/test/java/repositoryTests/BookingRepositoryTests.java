package repositoryTests;

import org.junit.jupiter.api.Assertions;
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
        User newOwner = userRepository.save(owner);

        Item item = itemGenerator.generateItem(newOwner.getId());
        Item newItem = itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(newItem.getId());

        User booker = userGenerator.generateUser();
        User newBooker = userRepository.save(booker);

        Booking booking = bookingGenerator.generateBooking(bookingRequest, newBooker, newItem);
        Booking newBooking = bookingRepository.save(booking);

        newBooker.getBookings().add(newBooking);
        newItem.getBookings().add(newBooking);

        Optional<User> checkUser = userRepository.findById(newBooker.getId());

        Assertions.assertTrue(checkUser.isPresent());
        Assertions.assertEquals(checkUser.get(), newBooker);

        Optional<Item> checkItem = itemRepository.findById(newItem.getId());

        Assertions.assertTrue(checkItem.isPresent());
        Assertions.assertEquals(checkItem.get(), newItem);

        Optional<Booking> check = bookingRepository.findById(newBooking.getId());

        Assertions.assertTrue(check.isPresent());
        Assertions.assertEquals(check.get(), newBooking);
    }
}
