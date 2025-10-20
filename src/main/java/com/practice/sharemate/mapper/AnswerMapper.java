package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.AnswerDTO;
import com.practice.sharemate.model.Answer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerMapper {
    public AnswerDTO entityToDto(Answer answer) {
        return AnswerDTO.builder()
                .id(answer.getId())
                .itemId(answer.getItem().getId())
                .name(answer.getItem().getName())
                .description(answer.getItem().getDescription())
                .requestId(answer.getRequest().getId())
                .available(answer.getItem().getAvailable())
                .build();
    }

    public List<AnswerDTO> listToDto(List<Answer> answers) {
        List<AnswerDTO> answerDTOS = new ArrayList<>();
        for (Answer answer : answers) {
            answerDTOS.add(entityToDto(answer));
        }
        return answerDTOS;
    }
}
