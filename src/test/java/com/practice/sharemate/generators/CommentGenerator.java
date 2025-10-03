package com.practice.sharemate.generators;

import com.practice.sharemate.model.Comment;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class CommentGenerator {
    public Comment generateComment() {
        return Comment.builder()
                .text(generateText())
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public String generateText() {
        return RandomString.make(32);
    }
}
