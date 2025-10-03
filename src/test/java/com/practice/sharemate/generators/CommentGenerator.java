package com.practice.sharemate.generators;

import com.practice.sharemate.model.Comment;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import net.bytebuddy.utility.RandomString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CommentGenerator {
    public Comment generateComment(Item item, User user) {
        return Comment.builder()
                .text(generateText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public String generateText() {
        return RandomString.make(32);
    }
}
