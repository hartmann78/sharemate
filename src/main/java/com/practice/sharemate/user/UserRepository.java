package com.practice.sharemate.user;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAllUsers();

    User findUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);
}
