package ru.practicum.main_svc.event;

import lombok.experimental.UtilityClass;
import ru.practicum.main_svc.category.CategoryMapper;
import ru.practicum.main_svc.event.dto.EventFullDto;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.main_svc.event.dto.NewEventDto;
import ru.practicum.main_svc.request.RequestRepository;
import ru.practicum.main_svc.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    RequestRepository requestRepository;

    public static Event toEvent(NewEventDto dto) {
        if (dto == null) {
            return null;
        }
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public static EventFullDto toDto(Event event, EventService service) {
        if (event == null) {
            return null;
        }
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(service.getViews(event.getId(), event.getCreatedOn()))
                .confirmedRequests(service.getConfirmedRequests(event.getId()))
                .build();
    }

    public static EventShortDto toShortDto(Event event, EventService service) {
        if (event == null) {
            return null;
        }

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(service.getViews(event.getId(), event.getCreatedOn()))
                .build();
    }
}
