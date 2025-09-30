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
            throw new BookingNotFoundException("Нельзя добавить комментарий без бронирования предмета");
        }

        if (!findBooking.getStatus().equals(Booking.BookingStatus.APPROVED)) {
            throw new ForbiddenException("Нельзя добавить комментарий без одобрения бронирования");
        }

        if (findBooking.getEnd().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Нельзя добавить комментарий до завершения бронирования!");
        }

        Item itemToComment = findItem.get();
        User author = findUser.get();

        comment.setAuthor(author);
        comment.setItem(itemToComment);
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        itemToComment.getComments().add(comment);
        author.getComments().add(comment);

        itemRepository.save(itemToComment);
        userRepository.save(author);

        return commentMapper.entityToDto(comment);
    }
}
