package ru.practicum.mainsvc.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("/users/{userId}/events")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid RequestCommentDto dto) {
        log.info("POST comments/ create: {}", dto);
        return commentService.create(userId, eventId, dto);
    }

    @PatchMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid RequestCommentDto dto) {
        log.info("PATCH comments/ updated: {}", dto);
        return commentService.update(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void getCommentById(
            @PathVariable Long commentId) {
        log.info("GET comments/ getById: commentId = {}", commentId);
        commentService.getById(commentId);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        log.info("DELETE comments/ deleted: userId = {}, commentId = {}", userId, commentId);
        commentService.delete(userId, commentId);
    }

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("GET commentsUser/ by userId - {}", userId);
        return commentService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("GET commentsUser/ by eventId - {}", eventId);
        return commentService.getAllByEventId(eventId, from, size);
    }
}

