package com.practice.sharemate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDTO {
    private Long id;
    private Long itemId;
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;
}
