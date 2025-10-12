package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.generators.*;
import com.practice.sharemate.model.*;
import com.practice.sharemate.repository.*;
import jakarta.validation.ConstraintViolationException;
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

    private final UserGenerator userGenerator = new UserGenerator();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final RequestGenerator requestGenerator = new RequestGenerator();
    private final AnswerGenerator answerGenerator = new AnswerGenerator();

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

        assertThrows(ConstraintViolationException.class, () -> itemRepository.save(itemOnRequest));
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

        assertThrows(ConstraintViolationException.class, () -> itemRepository.save(itemOnRequest));

        item.setName(null);
        item.setDescription(null);
        item.setAvailable(null);
        itemRepository.save(item);
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

        assertThrows(ConstraintViolationException.class, () -> itemRepository.save(itemOnRequest));
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
