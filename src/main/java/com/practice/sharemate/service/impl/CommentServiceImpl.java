package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.exceptions.*;
import com.practice.sharemate.mapper.CommentMapper;
import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.Comment;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.BookingRepository;
import com.practice.sharemate.repository.CommentRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> findAll(Long itemId) {
        if (itemId == null) {
            throw new BadRequestException("Неверный запрос");
        }

        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        List<Comment> comments = commentRepository.findAllCommentsByItemId(itemId);

        if (comments.isEmpty()) {
            throw new CommentNotFoundException("Комментарии не найдены!");
        }

        return commentMapper.listToDto(comments);
    }

    @Override
    public CommentDTO findCommentById(Long itemId, Long commentId) {
        if (itemId == null || commentId == null) {
            throw new BadRequestException("Неверный запрос");
        }

        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        Optional<Comment> comment = commentRepository.findCommentByItemIdAndCommentId(itemId, commentId);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException("Комментарий с id " + commentId + " не найден!");
        }

        return commentMapper.entityToDto(comment.get());
    }

    @Override
    public CommentDTO postComment(Long userId, Long itemId, Comment comment) {
        if (userId == null || itemId == null || comment == null) {
            throw new BadRequestException("Неверный запрос");
        }

        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        Booking findBooking = bookingRepository.findBookingByBookerIdAndItemId(userId, itemId);
        if (findBooking == null) {
            throw new BookingNotFoundException("Нельзя добавить комментарий без бронирования предмета!");
        }

        if (!findBooking.getStatus().equals(Booking.BookingStatus.APPROVED)) {
            throw new ForbiddenException("Нельзя добавить комментарий без одобрения бронирования!");
        }

        if (findBooking.getEnd().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Нельзя добавить комментарий до завершения бронирования!");
        }

        Item itemToComment = findItem.get();
        User author = findUser.get();

        comment.setAuthor(author);
        comment.setItem(itemToComment);
        comment.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        commentRepository.save(comment);

        itemToComment.getComments().add(comment);
        author.getComments().add(comment);

        itemRepository.save(itemToComment);
        userRepository.save(author);

        return commentMapper.entityToDto(comment);
    }

    @Override
    public CommentDTO updateComment(Long userId, Long itemId, Long commentId, Comment comment) {
        if (userId == null || itemId == null || commentId == null || comment == null) {
            throw new BadRequestException("Неверный запрос");
        }

        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new BadRequestException("Текст комментария не должен быть пустым!");
        }

        Optional<User> findAuthor = userRepository.findById(userId);
        if (findAuthor.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        Optional<Comment> findComment = commentRepository.findCommentByItemIdAndCommentIdAndAuthorId(itemId, userId, commentId);
        if (findComment.isEmpty()) {
            throw new CommentNotFoundException("Комментарий с id " + commentId + " не найден!");
        }

        Comment updateComment = findComment.get();

        updateComment.setText(comment.getText());
        updateComment.setUpdated(LocalDateTime.now());

        return commentMapper.entityToDto(commentRepository.save(updateComment));
    }

    @Override
    public void deleteComment(Long userId, Long itemId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException("Комментарий с id " + commentId + " не найден!");
        }

        if (!comment.get().getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Доступ воспрещён!");
        }

        commentRepository.deleteById(commentId);
    }
}
