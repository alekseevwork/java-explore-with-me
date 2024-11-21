package ru.practicum.mainsvc.comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    CommentDto create(Long userId, Long evenId, RequestCommentDto dto);

    CommentDto update(Long userId, Long evenId, RequestCommentDto dto);

    CommentDto getById(Long commentId);

    List<CommentDto> getAllByUserId(Long userId, int from, int size);

    List<CommentDto> getAllByEventId(Long eventId, int from, int size);

    void delete(Long userId, Long commentId);
}
