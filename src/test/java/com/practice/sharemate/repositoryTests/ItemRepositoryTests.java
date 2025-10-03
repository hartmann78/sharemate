package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.generators.*;
import com.practice.sharemate.model.*;
import com.practice.sharemate.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(user.getId(), checkItem.get().getOwnerId());
        assertEquals(item, checkItem.get());
    }

    @Test
    void searchItems() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        item.setAvailable(true);
        itemRepository.save(item);

        List<Item> searchItems = itemRepository.search(item.getName());

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(item, checkItem.get());
        assertTrue(searchItems.contains(item));
    }

    @Test
    void testPagination() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item1 = itemGenerator.generateItem(user.getId());
        item1.setName("item1");
        item1.setAvailable(true);
        itemRepository.save(item1);

        Item item2 = itemGenerator.generateItem(user.getId());
        item2.setName("item2");
        item2.setAvailable(true);
        itemRepository.save(item2);

        Item item3 = itemGenerator.generateItem(user.getId());
        item3.setName("item3");
        item3.setAvailable(true);
        itemRepository.save(item3);

        Item item4 = itemGenerator.generateItem(user.getId());
        item4.setName("item4");
        item4.setAvailable(true);
        itemRepository.save(item4);

        Item item5 = itemGenerator.generateItem(user.getId());
        item5.setName("item5");
        item5.setAvailable(true);
        itemRepository.save(item5);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Item> checkItem1 = itemRepository.findById(item1.getId());
        assertTrue(checkItem1.isPresent());
        assertEquals(item1, checkItem1.get());

        Optional<Item> checkItem2 = itemRepository.findById(item2.getId());
        assertTrue(checkItem2.isPresent());
        assertEquals(item2, checkItem2.get());

        Optional<Item> checkItem3 = itemRepository.findById(item3.getId());
        assertTrue(checkItem3.isPresent());
        assertEquals(item3, checkItem3.get());

        Optional<Item> checkItem4 = itemRepository.findById(item4.getId());
        assertTrue(checkItem4.isPresent());
        assertEquals(item4, checkItem4.get());

        Optional<Item> checkItem5 = itemRepository.findById(item5.getId());
        assertTrue(checkItem5.isPresent());
        assertEquals(item5, checkItem5.get());

        List<Item> items = itemRepository.searchPagination("item", 1, 3);
        assertEquals(3, items.size());
        assertTrue(items.containsAll(List.of(item2, item3, item4)));
    }

    @Test
    void findAllByOwnerIdPagination() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Item item1 = itemGenerator.generateItem(user.getId());
        itemRepository.save(item1);

        Item item2 = itemGenerator.generateItem(user.getId());
        itemRepository.save(item2);

        Item item3 = itemGenerator.generateItem(user.getId());
        itemRepository.save(item3);

        Item item4 = itemGenerator.generateItem(user.getId());
        itemRepository.save(item4);

        Item item5 = itemGenerator.generateItem(user.getId());
        itemRepository.save(item5);

        List<Item> items = itemRepository.findAllByOwnerIdPagination(user.getId(), 1, 3);
        assertEquals(3, items.size());
        assertTrue(items.containsAll(List.of(item2, item3, item4)));
    }
}
