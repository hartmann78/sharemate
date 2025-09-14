package com.practice.sharemate.user;

import java.util.Collection;

public interface UserService {
    Collection<User> findAllUsers();

    User findUserById(Long id);

    User addUser(User user);

    User updateUser(Long userId,User user);

    void deleteUser(Long id);
}
