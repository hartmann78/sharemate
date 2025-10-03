package com.practice.sharemate.service;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;

public interface CommentService {
    CommentDTO findCommentById(Long itemId, Long commentId);

    CommentDTO postComment(Long userId, Long itemId, Comment comment);

    void deleteComment(Long userId, Long itemId, Long commentId);
}
