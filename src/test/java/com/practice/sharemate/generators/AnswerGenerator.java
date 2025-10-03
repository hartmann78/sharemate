package com.practice.sharemate.generators;

import com.practice.sharemate.model.Answer;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.Request;

public class AnswerGenerator {
    public Answer generateAnswer(Item item, Request request) {
        return Answer.builder()
                .item(item)
                .request(request)
                .build();
    }
}
