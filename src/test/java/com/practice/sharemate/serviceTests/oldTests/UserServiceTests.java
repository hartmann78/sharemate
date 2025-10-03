package com.practice.sharemate.serviceTests.oldTests;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.mapper.UserMapper;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserMapper mockUserMapper;
    @Mock
    UserRepository mockUserRepository;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    private Long globalUserId = 0L;


    @Test
    public void createUser() {
        User user = generateUser();

        User checkUser = User.builder()
                .id(++globalUserId)
                .name(user.getName())
                .email(user.getEmail())
                .build();

        Mockito
                .when(mockUserRepository.save(user))
                .thenReturn(checkUser);

        Mockito
                .when(mockUserMapper.dtoToEntity(userServiceImpl.addUser(user)))
                .thenReturn(checkUser);

        User newUser = mockUserMapper.dtoToEntity(userServiceImpl.addUser(user));
        Assertions.assertEquals(checkUser, newUser);
    }

    @Test
    public void getUsers() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserRepository, mockUserMapper);

        User user = generateUser();

        User newUser = User.builder()
                .id(++globalUserId)
                .name(user.getName())
                .email(user.getEmail())
                .build();

        UserDTO newUserDto = UserDTO.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .build();

        Mockito
                .when(mockUserRepository.save(user))
                .thenReturn(newUser);

        Mockito
                .when(mockUserMapper.entityToDto(newUser))
                .thenReturn(newUserDto);

        UserDTO checkUser = userServiceImpl.addUser(user);

        List<User> users = List.of(newUser);
        List<UserDTO> usersDTO = List.of(newUserDto);

        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(users);

        Mockito
                .when(mockUserMapper.listToDto(users))
                .thenReturn(usersDTO);

        List<UserDTO> getUsers = userServiceImpl.findAllUsers();
        Assertions.assertTrue(getUsers.contains(checkUser));
    }

    public User generateUser() {
        return User.builder()
                .name(generateName())
                .email(generateEmail())
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    private String generateName() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateEmail() {
        return UUID.randomUUID().toString().substring(0, 8) + "@mail.com";
    }
}
