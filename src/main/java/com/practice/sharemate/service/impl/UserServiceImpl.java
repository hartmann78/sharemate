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
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return userMapper.listToDto(users);
    }

    @Override
    public UserDTO findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return userMapper.entityToDto(user.get());
        }

        throw new UserNotFoundException("Пользователь с id" + userId + " не найден!");
    }

    @Override
    public UserDTO addUser(User user) {
        if (user.getEmail() == null) {
            throw new BadRequestException("Неправильный запрос");
        }

        if (userRepository.findUserEmail(user.getEmail()) == true) {
            throw new EmailExistsException("Данный email уже существует!");
        }

        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long userId, User user) {
        // Проверка на null
        if (userId == null || user == null) {
            throw new BadRequestException("Неправильный запрос");
        }

        // Проверка на наличие email в базе данных
        if (user.getEmail() != null && userRepository.findUserEmail(user.getEmail()) == true) {
            throw new EmailExistsException("Данный email уже существует!");
        }

        // Проверки на наличие пользователей в базе данных по id
        Optional<User> findUser = userRepository.findById(userId);

        if (findUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id" + userId + " не найден!");
        }

        User updateUser = findUser.get();

        // Обновление данных
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }

        return userMapper.entityToDto(userRepository.save(updateUser));
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
