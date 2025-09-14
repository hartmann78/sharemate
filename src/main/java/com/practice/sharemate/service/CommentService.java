package com.practice.sharemate.service;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;

public interface CommentService {
    CommentDTO postComment(Long userId, Long itemId, Comment comment);
}
