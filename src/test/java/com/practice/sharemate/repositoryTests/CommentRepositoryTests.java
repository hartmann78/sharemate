package com.practice.sharemate.repositoryTests;

import com.practice.sharemate.generators.*;
import com.practice.sharemate.model.*;
import com.practice.sharemate.repository.BookingRepository;
import com.practice.sharemate.repository.CommentRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    private final UserGenerator userGenerator = new UserGenerator();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final BookingGenerator bookingGenerator = new BookingGenerator();
    private final BookingRequestGenerator bookingRequestGenerator = new BookingRequestGenerator();
    private final CommentGenerator commentGenerator = new CommentGenerator();

    private User owner;
    private Item item;
    private User booker;
    private BookingRequest bookingRequest;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    void create() {
        owner = userGenerator.generateUser();
        userRepository.save(owner);

        item = itemGenerator.generateItemWithOwner(owner);
        itemRepository.save(item);

        booker = userGenerator.generateUser();
        userRepository.save(booker);

        bookingRequest = bookingRequestGenerator.generateBookingRequest(item.getId());
        booking = bookingGenerator.generateBooking(bookingRequest, booker, item);
        booking.setStatus(Booking.BookingStatus.APPROVED);
        bookingRepository.save(booking);

        comment = commentGenerator.generateComment();
        comment.setItem(item);
        comment.setAuthor(booker);
        commentRepository.save(comment);
    }

    @Test
    void addComment() {
        Optional<Comment> checkComment = commentRepository.findById(comment.getId());
        assertTrue(checkComment.isPresent());
        assertEquals(comment, checkComment.get());
    }

    @Test
    void findAllCommentsByItemId() {
        List<Comment> findAllComments = commentRepository.findAllCommentsByItemId(item.getId());
        assertTrue(findAllComments.contains(comment));
    }

    @Test
    void findCommentByItemIdAndCommentId() {
        Optional<Comment> checkComment = commentRepository.findCommentByItemIdAndCommentId(item.getId(), comment.getId());
        assertTrue(checkComment.isPresent());
        assertEquals(comment, checkComment.get());
    }

    @Test
    void findCommentByItemIdAndAuthorIdAndCommentId() {
        Optional<Comment> checkComment = commentRepository.findCommentByItemIdAndAuthorIdAndCommentId(item.getId(), booker.getId(), comment.getId());
        assertTrue(checkComment.isPresent());
        assertEquals(comment, checkComment.get());
    }
}
