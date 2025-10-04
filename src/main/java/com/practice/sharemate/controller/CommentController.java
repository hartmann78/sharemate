package com.practice.sharemate.controller;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;
import com.practice.sharemate.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{itemId}/comment")
    public List<CommentDTO> findAll(@PathVariable Long itemId) {
        return commentService.findAll(itemId);
    }

    @GetMapping("/{itemId}/comment/{commentId}")
    public CommentDTO findCommentById(@PathVariable Long itemId,
                                      @PathVariable Long commentId) {
        return commentService.findCommentById(itemId, commentId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDTO postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId,
                                  @Valid @RequestBody Comment comment) {
        return commentService.postComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}/comment/{commentId}")
    public CommentDTO updateComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody Comment comment) {
        return commentService.updateComment(userId, itemId, commentId, comment);
    }

    @DeleteMapping("/{itemId}/comment/{commentId}")
    public void deleteComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, itemId, commentId);
    }
}
