package org.solen.controller.mappers;

import org.solen.controller.dto.user.UserDto;
import org.solen.domain.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}