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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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

    @Test
    void postComment() throws InterruptedException {
        User owner = userGenerator.generateUser();
        UserDTO ownerDTO = userService.addUser(owner);

        Item item = itemGenerator.generateAvailableItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(ownerDTO.getId(), item);

        User commentAuthor = userGenerator.generateUser();
        UserDTO commentAuthorDTO = userService.addUser(commentAuthor);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(itemDTO.getId());
        bookingRequest.setStart(LocalDateTime.now().plusSeconds(1));
        bookingRequest.setEnd(LocalDateTime.now().plusSeconds(2));

        BookingDTO bookingDTO = bookingService.addBooking(commentAuthorDTO.getId(), bookingRequest);

        bookingService.patchBooking(ownerDTO.getId(), bookingDTO.getId(), true);

        TimeUnit.SECONDS.sleep(3);

        Comment comment = commentGenerator.generateComment();
        CommentDTO commentDTO = commentService.postComment(commentAuthorDTO.getId(), itemDTO.getId(), comment);

        assertNotNull(commentDTO.getId());
        assertEquals(comment.getText(), commentDTO.getText());
        assertEquals(comment.getAuthor().getName(), commentDTO.getAuthorName());
        assertEquals(comment.getCreated(), commentDTO.getCreated());
    }

    @Test
    void deleteComment() throws InterruptedException {
        User owner = userGenerator.generateUser();
        UserDTO ownerDTO = userService.addUser(owner);

        Item item = itemGenerator.generateAvailableItemWithoutOwnerId();
        ItemDTO itemDTO = itemService.addItem(ownerDTO.getId(), item);

        User commentAuthor = userGenerator.generateUser();
        UserDTO commentAuthorDTO = userService.addUser(commentAuthor);

        BookingRequest bookingRequest = bookingRequestGenerator.generateBookingRequest(itemDTO.getId());
        bookingRequest.setStart(LocalDateTime.now().plusSeconds(1));
        bookingRequest.setEnd(LocalDateTime.now().plusSeconds(2));

        BookingDTO bookingDTO = bookingService.addBooking(commentAuthorDTO.getId(), bookingRequest);

        bookingService.patchBooking(ownerDTO.getId(), bookingDTO.getId(), true);

        TimeUnit.SECONDS.sleep(3);

        Comment comment = commentGenerator.generateComment();
        CommentDTO commentDTO = commentService.postComment(commentAuthorDTO.getId(), itemDTO.getId(), comment);

        commentService.deleteComment(commentAuthorDTO.getId(), itemDTO.getId(), commentDTO.getId());

        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(itemDTO.getId(), commentDTO.getId()));
    }
}
