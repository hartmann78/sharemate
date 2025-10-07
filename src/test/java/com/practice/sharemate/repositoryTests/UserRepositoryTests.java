package com.practice.sharemate.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.generators.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    private final UserGenerator userGenerator = new UserGenerator();

    private User user;

    @BeforeEach
    void create() {
        user = userGenerator.generateUser();
        userRepository.save(user);
    }

    @Test
    void createUserAndFindById() {
        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(user, checkUser.get());
    }

    @Test
    void getUsers() {
        List<User> findAllUsers = userRepository.findAll();
        assertTrue(findAllUsers.contains(user));
    }

    @Test
    void checkUserEmailExists() {
        Boolean check = userRepository.checkUserEmailExists(user.getEmail());
        assertTrue(check);
    }


    @Test
    void updateUser() {
        User updateUser = userGenerator.generateUser();

        if (updateUser.getName() != null && !updateUser.getName().equals(user.getName())) {
            user.setName(updateUser.getName());
        }

        if (updateUser.getEmail() != null && !updateUser.getEmail().equals(user.getEmail())) {
            user.setEmail(updateUser.getEmail());
        }

        userRepository.save(user);

        Optional<User> checkUpdatedUser = userRepository.findById(user.getId());
        assertTrue(checkUpdatedUser.isPresent());
        assertEquals(user, checkUpdatedUser.get());
    }

    @Test
    void deleteUser() {
        userRepository.deleteById(user.getId());

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isEmpty());
    }
}
