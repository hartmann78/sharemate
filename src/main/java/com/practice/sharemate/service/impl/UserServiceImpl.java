package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.mapper.UserMapper;
import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.exceptions.EmailExistsException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.UserService;
import com.practice.sharemate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return userMapper.listToDto(users);
    }

    @Override
    public UserDTO findUserById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        return userMapper.entityToDto(user.get());
    }

    @Override
    public UserDTO addUser(User user) {
        if (userRepository.checkUserEmail(user.getEmail()) == true) {
            throw new EmailExistsException("Данный email уже существует!");
        }

        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long userId, User user) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        if (user.getEmail() != null && userRepository.checkUserEmail(user.getEmail()) == true && !user.getEmail().equals(findUser.get().getEmail())) {
            throw new EmailExistsException("Данный email уже занят!");
        }

        User updateUser = findUser.get();

        if (user.getName() != null && !user.getName().equals(updateUser.getName())) {
            updateUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().equals(updateUser.getEmail())) {
            updateUser.setEmail(user.getEmail());
        }

        return userMapper.entityToDto(userRepository.save(updateUser));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
