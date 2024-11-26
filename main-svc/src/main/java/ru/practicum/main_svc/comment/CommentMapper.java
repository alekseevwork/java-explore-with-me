package ru.practicum.main_svc.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.main_svc.event.EventMapper;
import ru.practicum.main_svc.event.EventService;
import ru.practicum.main_svc.user.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentShortDto dto) {
        if (dto == null) {
            return null;
        }
        return Comment.builder()
                .text(dto.getText())
                .build();
    }

    public static CommentDto toDto(Comment comment, EventService eventService) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreated())
                .updateAt(comment.getUpdateAt() != null ? comment.getUpdateAt() : LocalDateTime.now())
                .author(UserMapper.toShortDto(comment.getAuthor()))
                .event(EventMapper.toShortDto(comment.getEvent(), eventService))
                .build();
    }

    public static CommentShortDto toShortDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentShortDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }
}
