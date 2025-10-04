package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    private final AnswerMapper answerMapper;

    public RequestDTO entityToDto(Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .requestorId(request.getRequestor().getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .updated(request.getUpdated())
                .answers(answerMapper.listToDto(request.getAnswers()))
                .build();
    }

    public List<RequestDTO> listToDto(List<Request> requests) {
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request request : requests) {
            requestDTOS.add(entityToDto(request));
        }
        return requestDTOS;
    }
}
