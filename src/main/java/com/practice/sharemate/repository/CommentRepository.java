package com.practice.sharemate.repository;

import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
