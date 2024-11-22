package ru.practicum.main_svc.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import ru.practicum.main_svc.category.dto.CategoryDto;
import ru.practicum.main_svc.event.Location;
import ru.practicum.main_svc.event.State;
import ru.practicum.main_svc.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {

    private Long id;
    private String annotation;
    private UserShortDto initiator;
    private CategoryDto category;
    private Long confirmedRequests;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    private Boolean paid;
    private int participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;
    private String title;
    private State state;
    private Long views;
}