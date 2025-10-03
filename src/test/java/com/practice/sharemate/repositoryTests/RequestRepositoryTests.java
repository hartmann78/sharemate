package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.generators.DateGenerator;
import com.practice.sharemate.generators.RequestGenerator;
import com.practice.sharemate.generators.UserGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RequestRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    UserGenerator userGenerator = new UserGenerator();
    RequestGenerator requestGenerator = new RequestGenerator();
    DateGenerator dateGenerator = new DateGenerator();

    @Test
    void createRequest() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Request request = requestGenerator.generateRequest(user);
        requestRepository.save(request);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(request, checkRequest.get());
    }

    @Test
    void getUserRequests() {
        User user = userGenerator.generateUser();
        userRepository.save(user);

        Request request1 = requestGenerator.generateRequest(user);
        request1.setCreated(dateGenerator.generateRandomDate());
        requestRepository.save(request1);

        Request request2 = requestGenerator.generateRequest(user);
        request2.setCreated(dateGenerator.generateRandomDate());
        requestRepository.save(request2);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());

        List<Request> userRequests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());
        assertTrue(userRequests.containsAll(List.of(request1, request2)));

        System.out.println(userRequests);
    }

    @Test
    void getUserRequestById() {
        User requestor = userGenerator.generateUser();
        userRepository.save(requestor);

        Request request = requestGenerator.generateRequest(requestor);
        requestRepository.save(request);

        Optional<User> checkUser = userRepository.findById(requestor.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(requestor, checkUser.get());

        Optional<Request> checkRequest = requestRepository.findById(request.getId());
        assertTrue(checkRequest.isPresent());
        assertEquals(requestor, checkRequest.get().getRequestor());
        assertEquals(request, checkRequest.get());
    }
}
