package com.practice.sharemate.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
