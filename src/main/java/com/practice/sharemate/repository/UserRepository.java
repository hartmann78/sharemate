package com.practice.sharemate.repository;

import com.practice.sharemate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u.email " +
            "from users as u " +
            "where u.email = ?1",
            nativeQuery = true)
    String findUserEmail(String email);
}
