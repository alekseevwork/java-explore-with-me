package ru.practicum.main_svc.user;

import ru.practicum.main_svc.user.dto.NewUserRequest;
import ru.practicum.main_svc.user.dto.UserDto;
import ru.practicum.main_svc.user.dto.UserShortDto;

public class UserMapper {

    public static User toUser(NewUserRequest dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserShortDto(user.getId(), user.getName());
    }
}
