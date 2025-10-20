package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.CommentDTO;
import com.practice.sharemate.model.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {
    public CommentDTO entityToDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    public List<CommentDTO> listToDto(List<Comment> comments) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(entityToDto(comment));
        }
        return commentDTOS;
    }
}
