package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.AnswerDTO;
import com.practice.sharemate.model.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public AnswerDTO entityToDto(Answer answer) {
        return AnswerDTO.builder()
                .id(answer.getId())
                .itemId(answer.getItemId())
                .requestId(answer.getRequest().getId())
                .description(answer.getDescription())
                .available(answer.getAvailable())
                .build();
    }
}
