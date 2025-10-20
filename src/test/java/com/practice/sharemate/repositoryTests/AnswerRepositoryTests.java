package com.practice.sharemate.repositoryTests;

import com.practice.sharemate.generators.AnswerGenerator;
import com.practice.sharemate.generators.ItemGenerator;
import com.practice.sharemate.generators.RequestGenerator;
import com.practice.sharemate.generators.UserGenerator;
import com.practice.sharemate.model.Answer;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.AnswerRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AnswerRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private final UserGenerator userGenerator = new UserGenerator();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final RequestGenerator requestGenerator = new RequestGenerator();
    private final AnswerGenerator answerGenerator = new AnswerGenerator();

    private User user;
    private Item item;
    private Request request;
    private Answer answer;

    @BeforeEach
    void create() {
        request = requestGenerator.generateRequest(user);
        requestRepository.save(request);

        user = userGenerator.generateUser();
        userRepository.save(user);

        item = itemGenerator.generateItemWithOwner(user);
        itemRepository.save(item);

        answer = answerGenerator.generateAnswer(item, request);
        answerRepository.save(answer);
    }

    @Test
    void addAnswer() {
        Optional<Answer> checkAnswer = answerRepository.findById(answer.getId());
        assertTrue(checkAnswer.isPresent());
        assertEquals(answer, checkAnswer.get());
    }
}
