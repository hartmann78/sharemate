package generators;

import net.bytebuddy.utility.RandomString;
import repositoryTests.model.Comment;
import repositoryTests.model.Item;
import repositoryTests.model.User;

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
