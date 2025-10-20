package com.practice.sharemate.serviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.exceptions.CommentNotFoundException;
import com.practice.sharemate.generators.BookingRequestGenerator;
import com.practice.sharemate.generators.CommentGenerator;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.model.Comment;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.service.BookingService;
import com.practice.sharemate.service.CommentService;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CommentServiceTests {
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    BookingService bookingService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserGenerator userGenerator;
    @Autowired
    ItemGenerator itemGenerator;
    @Autowired
    BookingRequestGenerator bookingRequestGenerator;
    @Autowired
    CommentGenerator commentGenerator;

    private User owner;
    private Item item;
    private User commentAuthor;
    private BookingRequest bookingRequest;
    private Comment comment;

    private UserDTO ownerDTO;
    private ItemDTO itemDTO;
    private UserDTO commentAuthorDTO;
    private BookingDTO bookingDTO;
    private CommentDTO commentDTO;

    @BeforeEach
    void create() throws InterruptedException {
        owner = userGenerator.generateUser();
        ownerDTO = userService.addUser(owner);

        item = itemGenerator.generateAvailableItem();
        itemDTO = itemService.addItem(ownerDTO.getId(), item);

        commentAuthor = userGenerator.generateUser();
        commentAuthorDTO = userService.addUser(commentAuthor);

        bookingRequest = bookingRequestGenerator.generateBookingRequest(itemDTO.getId());
        bookingRequest.setStart(LocalDateTime.now().plusSeconds(1));
        bookingRequest.setEnd(LocalDateTime.now().plusSeconds(2));

        bookingDTO = bookingService.addBooking(commentAuthorDTO.getId(), bookingRequest);

        bookingService.patchBooking(ownerDTO.getId(), bookingDTO.getId(), true);

        TimeUnit.SECONDS.sleep(2);

        comment = commentGenerator.generateComment();
        commentDTO = commentService.postComment(commentAuthorDTO.getId(), itemDTO.getId(), comment);
    }

    @Test
    void findAll() {
        List<CommentDTO> checkComments = commentService.findAll(itemDTO.getId());
        assertTrue(checkComments.contains(commentDTO));
    }

    @Test
    void findCommentById() {
        CommentDTO checkComment = commentService.findCommentById(itemDTO.getId(), commentDTO.getId());
        assertEquals(commentDTO, checkComment);
    }

    @Test
    void postComment() {
        assertNotNull(commentDTO.getId());
        assertNotNull(commentDTO.getCreated());

        assertEquals(comment.getText(), commentDTO.getText());
        assertEquals(commentAuthorDTO.getName(), commentDTO.getAuthorName());
    }

    @Test
    void updateComment() {
        Comment update = commentGenerator.generateComment();
        CommentDTO updatedCommentDTO = commentService.updateComment(commentAuthorDTO.getId(), itemDTO.getId(), commentDTO.getId(), update);

        assertNotNull(updatedCommentDTO.getUpdated());

        assertEquals(update.getText(), updatedCommentDTO.getText());

        assertEquals(commentDTO.getId(), updatedCommentDTO.getId());
        assertEquals(commentDTO.getAuthorName(), updatedCommentDTO.getAuthorName());
        assertEquals(commentDTO.getCreated(), updatedCommentDTO.getCreated());
    }

    @Test
    void deleteComment() {
        commentService.deleteComment(commentAuthorDTO.getId(), itemDTO.getId(), commentDTO.getId());

        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(itemDTO.getId(), commentDTO.getId()));
    }
}
