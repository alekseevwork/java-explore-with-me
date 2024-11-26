package ru.practicum.main_svc.comment.controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.comment.CommentDto;
import ru.practicum.main_svc.comment.CommentService;
import ru.practicum.main_svc.comment.CommentShortDto;

@Slf4j
@Data
@RestController
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentShortDto dto) {
        log.info("PATCH /admin/comments/ updated: {}", dto);
        return commentService.updateByAdmin(commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long commentId) {
        log.info("DELETE /admin/comments/ deleted: commentId = {}", commentId);
        commentService.deleteAdminById(commentId);
    }
}
