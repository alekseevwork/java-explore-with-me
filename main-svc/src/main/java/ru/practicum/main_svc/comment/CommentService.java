package ru.practicum.main_svc.comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    CommentDto create(Long userId, Long evenId, CommentShortDto dto);

    CommentDto update(Long userId, Long evenId, CommentShortDto dto);

    CommentDto getById(Long commentId);

    List<CommentShortDto> getAllByUserId(Long userId, int from, int size);

    List<CommentShortDto> getAllByEventId(Long eventId, int from, int size);

    void deleteById(Long userId, Long commentId);
}
