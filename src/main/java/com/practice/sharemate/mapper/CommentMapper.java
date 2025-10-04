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

    public Comment dtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .text(commentDTO.getText())
                .created(commentDTO.getCreated())
                .updated(commentDTO.getUpdated())
                .build();
    }

    public List<CommentDTO> listToDto(List<Comment> comments) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(entityToDto(comment));
        }
        return commentDTOS;
    }

    public List<Comment> listToEntity(List<CommentDTO> commentDTOS) {
        List<Comment> comments = new ArrayList<>();
        for (CommentDTO comment : commentDTOS) {
            comments.add(dtoToEntity(comment));
        }
        return comments;
    }
}
