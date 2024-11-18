package ru.practicum.main_svc.user;

import ru.practicum.main_svc.user.dto.NewUserRequest;
import ru.practicum.main_svc.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto create(NewUserRequest request);

    void delete(long userId);
}
