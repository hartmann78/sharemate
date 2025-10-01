package com.practice.sharemate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestDTO {
    private Long id;
    private Long requestorId;
    private String description;
    private LocalDateTime created;
    private List<AnswerDTO> answers;
}
