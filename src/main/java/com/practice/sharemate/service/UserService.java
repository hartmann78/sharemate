package com.practice.sharemate.service;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    UserDTO addUser(User user);

    UserDTO updateUser(Long userId, User user);

    void deleteUser(Long id);
}
