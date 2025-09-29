package repositoryTests;

import com.practice.sharemate.exceptions.BadRequestException;
import generators.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import repositoryTests.model.*;
import repositoryTests.repository.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    CommentRepository commentRepository;

    UserGenerator userGenerator = new UserGenerator();
    ItemGenerator itemGenerator = new ItemGenerator();
    RequestGenerator requestGenerator = new RequestGenerator();
    AnswerGenerator answerGenerator = new AnswerGenerator();
    BookingGenerator bookingGenerator = new BookingGenerator();
    BookingRequestGenerator bookingRequestGenerator = new BookingRequestGenerator();
    CommentGenerator commentGenerator = new CommentGenerator();

    @Test
    void createItem() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        itemRepository.save(item);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());
    }

    @Test
    void createItemOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request requestOnItem = requestGenerator.generateRequest(requestor);
        requestRepository.save(requestOnItem);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItem(owner.getId(), requestOnItem.getId());
        itemRepository.save(itemOnRequest);

        Answer answer = answerGenerator.generateAnswer(itemOnRequest, requestOnItem);
        answerRepository.save(answer);

        requestOnItem.getAnswers().add(answer);
        requestRepository.save(requestOnItem);

        Optional<User> checkOwner = userRepository.findById(owner.getId());
        assertTrue(checkOwner.isPresent());
        assertEquals(owner, checkOwner.get());

        Optional<Item> checkItemOnRequest = itemRepository.findById(itemOnRequest.getId());
        assertTrue(checkItemOnRequest.isPresent());
        assertEquals(itemOnRequest, checkItemOnRequest.get());

        Optional<User> checkRequestor = userRepository.findById(requestor.getId());
        assertTrue(checkRequestor.isPresent());
        assertEquals(requestor, checkRequestor.get());

        Optional<Request> checkRequestOnItem = requestRepository.findById(requestOnItem.getId());
        assertTrue(checkRequestOnItem.isPresent());
        assertEquals(requestOnItem, checkRequestOnItem.get());
    }

    @Test
    void createItemWithoutNameOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request requestOnItem = requestGenerator.generateRequest(requestor);
        requestRepository.save(requestOnItem);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItem(owner.getId(), requestOnItem.getId());
        itemOnRequest.setName(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            if (itemOnRequest.getName() == null || itemOnRequest.getName().isBlank()) {
                throw new BadRequestException("Название предмета не должно быть пустым!");
            }
        });

        String expectedMessage = "Название предмета не должно быть пустым!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createItemWithoutDescriptionOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request requestOnItem = requestGenerator.generateRequest(requestor);
        requestRepository.save(requestOnItem);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItem(owner.getId(), requestOnItem.getId());
        itemOnRequest.setDescription(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            if (itemOnRequest.getDescription() == null || itemOnRequest.getDescription().isBlank()) {
                throw new BadRequestException("Описание предмета не должно быть пустым!");
            }
        });

        String expectedMessage = "Описание предмета не должно быть пустым!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createItemWithoutAvailableOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request requestOnItem = requestGenerator.generateRequest(requestor);
        requestRepository.save(requestOnItem);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItem(owner.getId(), requestOnItem.getId());
        itemOnRequest.setAvailable(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            if (itemOnRequest.getAvailable() == null) {
                throw new BadRequestException("Статус предмета не должен быть пустым!");
            }
        });

        String expectedMessage = "Статус предмета не должен быть пустым!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addComment() {
        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        Item item = itemGenerator.generateItem(owner.getId());
        item.setAvailable(true);
        itemRepository.save(item);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        booking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(booking);

        booker.getBookings().add(booking);
        item.getBookings().add(booking);

        Comment comment = commentGenerator.generateComment(item, booker);
        commentRepository.save(comment);

        item.getComments().add(comment);
        booker.getComments().add(comment);

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

        Optional<Comment> checkComment = commentRepository.findById(comment.getId());
        assertTrue(checkComment.isPresent());
        assertEquals(comment, checkComment.get());
    }

    @Test
    void updateItem() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        itemRepository.save(item);

        item.setName(itemGenerator.generateName());
        item.setDescription(itemGenerator.generateDescription());
        item.setAvailable(!item.getAvailable());

        itemRepository.save(item);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());
    }

    @Test
    void getAllUserItems() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        itemRepository.save(item);

        List<Item> allUserItems = itemRepository.findAllByOwnerId(user.getId());

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        assertTrue(allUserItems.contains(item));
    }

    @Test
    void getUserItem() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        itemRepository.save(item);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Item checkItem = itemRepository.findByIdAndOwnerId(item.getId(), user.getId());
        assertEquals(item, checkItem);
    }

    @Test
    void searchItems() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        item.setAvailable(true);
        itemRepository.save(item);

        String itemName = item.getName();
        String itemDescription = item.getDescription();

        List<Item> searchItems = itemRepository.findAllByNameIgnoreCaseAndAvailableTrueOrDescriptionIgnoreCaseAndAvailableTrue(itemName, itemDescription);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());

        assertTrue(searchItems.contains(item));
    }
}
