package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.generators.RequestGenerator;
import com.practice.sharemate.generators.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RequestRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    private UserGenerator userGenerator = new UserGenerator();
    private RequestGenerator requestGenerator = new RequestGenerator();

    private User user;
    private Request request;

    @BeforeEach
    void create() {
        user = userGenerator.generateUser();
        userRepository.save(user);

        request = requestGenerator.generateRequest(user);
        requestRepository.save(request);
    }

    @Test
    void createRequest() {
        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(request, checkRequest.get());
    }

    @Test
    void getUserRequests() {
        List<Request> userRequests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());
        assertTrue(userRequests.contains(request));
    }

    @Test
    void getUserRequestById() {
        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(user, checkRequest.get().getRequestor());
        assertEquals(request, checkRequest.get());
    }
}
