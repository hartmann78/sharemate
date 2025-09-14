package com.practice.sharemate.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDTO> comments;
}
