package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.RequestNotFoundException;
import com.practice.sharemate.generators.RequestGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.RequestService;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RequestServiceTests {
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;
    @Autowired
    UserGenerator userGenerator;
    @Autowired
    RequestGenerator requestGenerator;

    private User user;
    private UserDTO userDTO;

    private Request request;
    private RequestDTO requestDTO;

    @BeforeEach
    void create() {
        user = userGenerator.generateUser();
        userDTO = userService.addUser(user);

        request = requestGenerator.generateRequest();
        requestDTO = requestService.addRequest(userDTO.getId(), request);
    }

    @Test
    void findAllWithoutUserId() {
        List<RequestDTO> checkRequests = requestService.findAll(null);
        assertTrue(checkRequests.contains(requestDTO));
    }

    @Test
    void findAllWithUserId() {
        List<RequestDTO> checkRequests = requestService.findAll(userDTO.getId());
        assertTrue(checkRequests.contains(requestDTO));
    }

    @Test
    void findAllPagination() {
        List<RequestDTO> checkRequests = requestService.findAllPagination(0, 20);
        assertTrue(checkRequests.contains(requestDTO));
    }

    @Test
    void findUserRequestById() {
        RequestDTO checkRequests = requestService.findUserRequestById(userDTO.getId(), requestDTO.getId());
        assertEquals(requestDTO, checkRequests);
    }

    @Test
    void addRequest() {
        assertNotNull(requestDTO.getId());
        assertNotNull(requestDTO.getAnswers());
        assertNotNull(requestDTO.getCreated());

        assertEquals(userDTO.getId(), requestDTO.getRequestorId());
        assertEquals(request.getDescription(), requestDTO.getDescription());
    }

    @Test
    void updateRequest() {
        Request update = requestGenerator.generateRequest();
        RequestDTO updatedRequestDTO = requestService.updateRequest(userDTO.getId(), requestDTO.getId(), update);

        assertNotNull(updatedRequestDTO.getCreated());

        assertEquals(update.getDescription(), updatedRequestDTO.getDescription());

        assertEquals(requestDTO.getId(), updatedRequestDTO.getId());
        assertEquals(requestDTO.getRequestorId(), updatedRequestDTO.getRequestorId());
        assertEquals(requestDTO.getCreated(), updatedRequestDTO.getCreated());
        assertEquals(requestDTO.getAnswers(), updatedRequestDTO.getAnswers());
    }

    @Test
    void deleteRequest() {
        requestService.deleteRequest(userDTO.getId(), requestDTO.getId());

        assertThrows(RequestNotFoundException.class, () -> requestService.findUserRequestById(userDTO.getId(), requestDTO.getId()));
    }
}
