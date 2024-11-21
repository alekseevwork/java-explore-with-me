package ru.practicum.main_svc.comment;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.error.exception.RequestConflictException;
import ru.practicum.main_svc.event.Event;
import ru.practicum.main_svc.event.EventRepository;
import ru.practicum.main_svc.event.EventService;
import ru.practicum.main_svc.event.State;
import ru.practicum.main_svc.user.User;
import ru.practicum.main_svc.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Service
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService {

    private  final CommentRepository commentRepository;
    private  final UserRepository userRepository;
    private  final EventRepository eventRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, CommentShortDto dto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User by id - " + userId + " not found"));

        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event by id - " + eventId + " not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestConflictException("Event not published");
        }

        Comment comment = CommentMapper.toComment(dto);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setEvent(event);

        return CommentMapper.toDto(commentRepository.save(comment), eventService);
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, Long commentId, CommentShortDto dto) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User by id - " + userId + " not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by id - " + commentId + " not found"));

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RequestConflictException("User by id - " + userId + " not author comment by id - " + commentId);
        }
        comment.setUpdateAt(LocalDateTime.now());
        comment.setText(dto.getText());

        return CommentMapper.toDto(commentRepository.save(comment), eventService);
    }

    @Override
    public CommentDto getById(Long commentId) {
        return CommentMapper.toDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by id - " + commentId + " not found")), eventService);
    }

    @Override
    public List<CommentShortDto> getAllByUserId(Long userId, int from, int size) {
        return commentRepository.findAllByAuthorId(userId, PageRequest.of(from, size))
                .stream().map(CommentMapper::toShortDto).toList();
    }

    @Override
    public List<CommentShortDto> getAllByEventId(Long eventId, int from, int size) {
        return commentRepository.findAllByEventId(eventId, PageRequest.of(from, size))
                .stream().map(CommentMapper::toShortDto).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by id - " + commentId + " not found"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RequestConflictException("User not author this comment");
        };
        commentRepository.deleteById(commentId);
    }
}
