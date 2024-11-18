package ru.practicum.main_svc.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.error.exception.DuplicationNameException;
import ru.practicum.main_svc.user.dto.NewUserRequest;
import ru.practicum.main_svc.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(PageRequest.of(from, size)).toList();
        } else {
            users =  userRepository.findByIdIn(ids, PageRequest.of(from, size));
        }
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto create(NewUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicationNameException("Email " + request.getEmail() + " already exist");
        }
        return UserMapper.toDto(userRepository.save(UserMapper.toUser(request)));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
