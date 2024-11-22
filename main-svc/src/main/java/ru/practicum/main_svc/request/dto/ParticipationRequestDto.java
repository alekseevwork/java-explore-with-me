package ru.practicum.main_svc.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {

    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private long event;
    private long requester;
    private String status;
}