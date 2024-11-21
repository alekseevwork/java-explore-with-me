package ru.practicum.main_svc.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
        log.info("POST comments/ create: {}", dto);
        return commentService.create(userId, eventId, dto);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @RequestBody @Valid CommentShortDto dto) {
        log.info("PATCH comments/ updated: {}", dto);
        return commentService.update(userId, eventId, dto);
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

