package ru.practicum.mainsvc.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    Long id;
    String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    Long author;
    Long event;
}
