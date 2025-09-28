package generators;

import repositoryTests.model.Answer;
import repositoryTests.model.Item;
import repositoryTests.model.Request;

public class AnswerGenerator {
    public Answer generateAnswer(Item item, Request request) {
        return Answer.builder()
                .itemId(item.getId())
                .request(request)
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
