package ru.practicum.mainsvc.comment;

public class CommentMapper {

    public static Comment toComment(RequestCommentDto dto) {
        if (dto == null) {
            return null;
        }
        return Comment.builder()
                .text(dto.getText())
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreated())
                .author(comment.getAuthor())
                .event(comment.getEvent())
                .build();
    }
}
