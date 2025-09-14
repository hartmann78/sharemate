package com.practice.sharemate.user;

import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.exceptions.EmailExistsException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private Long globalUserId = 0L;
    private final Map<Long, String> emails;
    private final UserRepository userRepository;

    @Override
    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new UserNotFoundException("Пользователь с id" + id + " не найден!");
        }

        return user;
    }

    @Override
    public User addUser(User user) {
        if (user.getEmail() == null) {
            throw new BadRequestException("Неправильный запрос");
        } else if (!emails.containsValue(user.getEmail())) {
            user.setId(++globalUserId);
            emails.put(user.getId(), user.getEmail());
            return userRepository.addUser(user);
        } else {
            throw new EmailExistsException("Данный email уже существует!");
        }
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userToUpdate = userRepository.findUserById(userId);

        if (emails.containsValue(user.getEmail()) && !user.getEmail().equals(userToUpdate.getEmail())) {
            throw new EmailExistsException("Данный email уже существует!");
        }

        if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
            emails.replace(userId, user.getEmail());
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        return userRepository.updateUser(userToUpdate);
    }


    @Override
    public void deleteUser(Long id) {
        User userToDelete = userRepository.findUserById(id);
        emails.remove(userToDelete.getId());
        userRepository.deleteUser(userToDelete);
    }
}
