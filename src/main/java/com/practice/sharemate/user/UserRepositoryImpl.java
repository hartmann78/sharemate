package com.practice.sharemate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User findUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }
}
