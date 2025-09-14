package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.dto.CommentMapper;
import com.practice.sharemate.exceptions.BadRequestException;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.model.Comment;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
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

        if (LocalDateTime.now().isBefore(commentRepository.findBooking(userId, itemId).getEnd())) {
            throw new BadRequestException("Нельзя добавить комментарий до завершения бронирования!");
        }

        Item itemToComment = findItem.get();

        comment.setAuthor(findUser.get());
        comment.setItem(itemToComment);
        comment.setCreated(LocalDateTime.now());

        Comment newComment = commentRepository.save(comment);

        itemToComment.getComments().add(newComment);

        itemRepository.save(itemToComment);

        return commentMapper.entityToDto(newComment);
    }
}
