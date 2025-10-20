package com.practice.sharemate.repository;

import com.practice.sharemate.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c.id, c.text, c.item_id, c.author_id, c.created, c.updated " +
            "from comments c " +
            "inner join items i on i.id = c.item_id " +
            "where i.id = ?1",
            nativeQuery = true)
    List<Comment> findAllCommentsByItemId(Long itemId);

    @Query(value = "select c.id, c.text, c.item_id, c.author_id, c.created, c.updated " +
            "from comments c " +
            "inner join items i on i.id = c.item_id " +
            "where i.id = ?1 " +
            "and c.id = ?2",
            nativeQuery = true)
    Optional<Comment> findCommentByItemIdAndCommentId(Long itemId, Long commentId);

    @Query(value = "select c.id, c.text, c.item_id, c.author_id, c.created, c.updated " +
            "from comments c " +
            "inner join users u on u.id = c.author_id " +
            "inner join items i on i.id = c.item_id " +
            "where i.id = ?1 " +
            "  and u.id = ?2 " +
            "  and c.id = ?3",
            nativeQuery = true)
    Optional<Comment> findCommentByItemIdAndAuthorIdAndCommentId(Long itemId, Long userId, Long commentId);
}
