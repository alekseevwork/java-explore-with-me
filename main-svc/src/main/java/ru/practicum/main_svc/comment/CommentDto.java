package ru.practicum.main_svc.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main_svc.event.Event;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.main_svc.user.User;
import ru.practicum.main_svc.user.dto.UserDto;
import ru.practicum.main_svc.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    private UserShortDto author;
    private EventShortDto event;
}
