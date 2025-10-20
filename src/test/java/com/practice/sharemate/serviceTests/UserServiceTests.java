package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    UserGenerator userGenerator;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void create() {
        user = userGenerator.generateUser();
        userDTO = userService.addUser(user);
    }

    @Test
    void findAll() {
        List<UserDTO> checkUsers = userService.findAll();
        assertTrue(checkUsers.contains(userDTO));
    }

    @Test
    void findUserById() {
        UserDTO checkUser = userService.findUserById(userDTO.getId());
        assertEquals(userDTO, checkUser);
    }

    @Test
    void addUser() {
        assertNotNull(userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void updateUser() {
        User updateUser = userGenerator.generateUser();

        UserDTO updatedUserDTO = userService.updateUser(userDTO.getId(), updateUser);
        assertNotNull(updatedUserDTO.getId());
        assertEquals(updateUser.getName(), updatedUserDTO.getName());
        assertEquals(updateUser.getEmail(), updatedUserDTO.getEmail());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(userDTO.getId());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userDTO.getId()));
    }
}
