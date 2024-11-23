package ru.practicum.main_svc.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_svc.comment.CommentDto;
import ru.practicum.main_svc.comment.CommentService;
import ru.practicum.main_svc.comment.CommentShortDto;

import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @RequestBody @Valid CommentShortDto dto) {
        log.info("POST comments/ by user id - {}, create: {}", userId, dto);
        return commentService.create(userId, eventId, dto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentShortDto dto) {
        log.info("PATCH comments/ by user id - {},  updated: {}", userId, dto);
        return commentService.update(userId, commentId, dto);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getById(@PathVariable Long commentId) {
        log.info("GET comments/ getById: commentId = {}", commentId);
        return commentService.getById(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        log.info("DELETE comments/ deleted: userId = {}, commentId = {}", userId, commentId);
        commentService.deleteById(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentShortDto> getUserComments(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size) {

        log.info("GET commentsUser/ by userId - {}", userId);
        return commentService.getAllByUserId(userId, from, size);
    }
}

