package com.practice.sharemate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDTO {
    private Long id;
    private Long itemId;
    private Long requestId;
    private String description;
    private Boolean available;
}
