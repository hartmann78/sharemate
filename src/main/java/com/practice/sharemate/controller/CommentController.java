package com.practice.sharemate.controller;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;
import com.practice.sharemate.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{itemId}/comment")
    public CommentDTO postComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody Comment comment) {
        return commentService.postComment(userId, itemId, comment);
    }
}
