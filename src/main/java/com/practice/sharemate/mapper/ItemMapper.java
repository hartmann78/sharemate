package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentMapper commentMapper;

    public ItemDTO entityToDto(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(commentMapper.listToDto(item.getComments()))
                .build();
    }

    public List<ItemDTO> listToDto(List<Item> items) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (Item item : items) {
            itemDTOS.add(entityToDto(item));
        }
        return itemDTOS;
    }
}
