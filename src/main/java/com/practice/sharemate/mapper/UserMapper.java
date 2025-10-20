package com.practice.sharemate.mapper;

import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDTO entityToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDTO> listToDto(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(entityToDto(user));
        }
        return userDTOS;
    }
}
