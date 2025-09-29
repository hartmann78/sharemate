package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.AnswerDTO;
import com.practice.sharemate.model.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public AnswerDTO entityToDto(Answer answer) {
        return AnswerDTO.builder()
                .id(answer.getId())
                .itemId(answer.getItem().getId())
                .requestId(answer.getRequest().getId())
                .description(answer.getItem().getDescription())
                .available(answer.getItem().getAvailable())
                .build();
    }
}
