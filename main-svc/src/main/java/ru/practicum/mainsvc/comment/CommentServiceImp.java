package ru.practicum.mainsvc.comment;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Service
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService {

    private  final CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDto create(Long userId, Long eventId, RequestCommentDto dto) {
        if (userId == null) {
            throw new RuntimeException();
        }

        if (eventId == null) {
            throw new RuntimeException();
        }
        Comment comment = CommentMapper.toComment(dto);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(userId);
        comment.setEvent(eventId);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, Long eventId, RequestCommentDto dto) {

        Comment comment = commentRepository.findByAuthorIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setCreated(LocalDateTime.now());
        comment.setText(dto.getText());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getById(Long commentId) {
        return CommentMapper.toDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    @Override
    public List<CommentDto> getAllByUserId(Long userId, int from, int size) {
        return commentRepository.findAllByAuthorId(userId, PageRequest.of(from, size))
                .stream().map(CommentMapper::toDto).toList();
    }

    @Override
    public List<CommentDto> getAllByEventId(Long eventId, int from, int size) {
        return commentRepository.findAllByEventId(eventId, PageRequest.of(from, size))
                .stream().map(CommentMapper::toDto).toList();
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {
        if (!commentRepository.findById(commentId).orElseThrow().getAuthor().equals(userId)) {
            throw new RuntimeException("User not author this comment");
        };
        commentRepository.deleteById(commentId);
    }
}
