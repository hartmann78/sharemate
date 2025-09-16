package com.practice.sharemate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDTO {
    private Long id;
    private Long itemId;
    private String description;
    private Boolean available;
    private LocalDateTime created;
}
