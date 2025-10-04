package com.practice.sharemate.service;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findAll(Long itemId);

    CommentDTO findCommentById(Long itemId, Long commentId);

    CommentDTO postComment(Long userId, Long itemId, Comment comment);

    CommentDTO updateComment(Long userId, Long itemId, Long commentId, Comment comment);

    void deleteComment(Long userId, Long itemId, Long commentId);
}
