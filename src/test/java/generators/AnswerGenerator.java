package generators;

import repositoryTests.model.Answer;
import repositoryTests.model.Item;
import repositoryTests.model.Request;

public class AnswerGenerator {
    public Answer generateAnswer(Item item, Request request) {
        return Answer.builder()
                .item(item)
                .request(request)
                .build();
    }
}
