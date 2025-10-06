package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.generators.*;
import com.practice.sharemate.model.*;
import com.practice.sharemate.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    private UserGenerator userGenerator = new UserGenerator();
    private ItemGenerator itemGenerator = new ItemGenerator();
    private RequestGenerator requestGenerator = new RequestGenerator();
    private AnswerGenerator answerGenerator = new AnswerGenerator();
    private BookingGenerator bookingGenerator = new BookingGenerator();
    private BookingRequestGenerator bookingRequestGenerator = new BookingRequestGenerator();
    private CommentGenerator commentGenerator = new CommentGenerator();

    private User owner;
    private Item item;

    @BeforeEach
    void create() {
        owner = userGenerator.generateUser();
        userRepository.save(owner);

        item = itemGenerator.generateItemWithOwner(owner);
        itemRepository.save(item);
    }

    @Test
    void createItem() {
        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());
    }

    @Test
    void createItemOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request request = requestGenerator.generateRequest(requestor);
        requestRepository.save(request);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItemWithOwnerIdAndRequestId(owner, request.getId());
        itemRepository.save(itemOnRequest);

        Answer answer = answerGenerator.generateAnswer(itemOnRequest, request);
        answerRepository.save(answer);

        request.getAnswers().add(answer);
        requestRepository.save(request);

        Optional<Item> checkItemOnRequest = itemRepository.findById(itemOnRequest.getId());
        assertTrue(checkItemOnRequest.isPresent());
        assertEquals(itemOnRequest, checkItemOnRequest.get());

        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(request, checkRequest.get());
    }

    @Test
    void createItemWithoutNameOnRequest() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request requestOnItem = requestGenerator.generateRequest(requestor);
        requestRepository.save(requestOnItem);

        User owner = userGenerator.generateUser();
        userRepository.save(owner);

        Item itemOnRequest = itemGenerator.generateItemWithOwnerIdAndRequestId(owner, requestOnItem.getId());
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

        Item itemOnRequest = itemGenerator.generateItemWithOwnerIdAndRequestId(owner, requestOnItem.getId());
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

        Item itemOnRequest = itemGenerator.generateItemWithOwnerIdAndRequestId(owner, requestOnItem.getId());
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
        User booker = userGenerator.generateUser();
        userRepository.save(booker);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        Booking booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        booking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Comment comment = commentGenerator.generateComment();
        comment.setItem(item);
        comment.setAuthor(booker);
        commentRepository.save(comment);

        Optional<Comment> checkComment = commentRepository.findById(comment.getId());
        assertTrue(checkComment.isPresent());
        assertEquals(comment, checkComment.get());
    }

    @Test
    void updateItem() {
        item.setName(itemGenerator.generateName());
        item.setDescription(itemGenerator.generateDescription());
        item.setAvailable(!item.getAvailable());

        itemRepository.save(item);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());
    }

    @Test
    void getAllUserItems() {
        List<Item> allUserItems = itemRepository.findAllByOwnerId(owner.getId());
        assertTrue(allUserItems.contains(item));
    }

    @Test
    void getUserItem() {
        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(owner.getId(), checkItem.get().getOwner().getId());
        assertEquals(item, checkItem.get());
    }

    @Test
    void searchItems() {
        item.setAvailable(true);
        List<Item> searchItems = itemRepository.search(item.getName());
        assertTrue(searchItems.contains(item));
    }

    @Test
    void searchPagination() {
        item.setAvailable(true);
        List<Item> items = itemRepository.searchPagination(item.getName(), 0, 1);
        assertTrue(items.contains(item));
    }

    @Test
    void findAllByOwnerIdPagination() {
        List<Item> items = itemRepository.findAllByOwnerIdPagination(owner.getId(), 0, 1);
        assertTrue(items.contains(item));
    }
}
