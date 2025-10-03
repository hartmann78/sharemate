package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.UserService;
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

    @Test
    void findAll() {
        User user1 = userGenerator.generateUser();
        UserDTO userDTO1 = userService.addUser(user1);

        User user2 = userGenerator.generateUser();
        UserDTO userDTO2 = userService.addUser(user2);

        List<UserDTO> checkUsers = userService.findAll();
        assertTrue(checkUsers.containsAll(List.of(userDTO1, userDTO2)));
    }

    @Test
    void findUserById() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        UserDTO checkUser = userService.findUserById(userDTO.getId());
        assertEquals(userDTO, checkUser);
    }

    @Test
    void addUser() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        assertNotNull(userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void updateUser() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        Long userId = userDTO.getId();
        User updateUser = userGenerator.generateUser();

        UserDTO updatedUserDTO = userService.updateUser(userId, updateUser);
        assertEquals(updatedUserDTO, userService.findUserById(updatedUserDTO.getId()));
    }

    @Test
    void deleteUser() {
        User user = userGenerator.generateUser();
        UserDTO userDTO = userService.addUser(user);

        userService.deleteUser(userDTO.getId());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userDTO.getId()));
    }
}
