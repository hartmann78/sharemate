package com.practice.sharemate.generators;

import com.practice.sharemate.model.Comment;
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
                .updated(null)
                .build();
    }

    public String generateText() {
        return RandomString.make(32);
    }
}
