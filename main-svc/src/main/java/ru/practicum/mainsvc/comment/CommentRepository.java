package ru.practicum.mainsvc.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthorId(Long userId, PageRequest pageRange);

    List<Comment> findAllByEventId(Long eventId, PageRequest pageRange);

    Optional<Comment> findByAuthorIdAndEventId(Long userId, Long eventId);
}
