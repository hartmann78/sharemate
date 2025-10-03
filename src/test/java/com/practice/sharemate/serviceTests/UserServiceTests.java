package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    UserGenerator userGenerator = new UserGenerator();

    @Test
    void createUser() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        assertNotNull(userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
    }
}
