package ru.practicum.main_svc.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.event.EventService;
import ru.practicum.main_svc.event.dto.EventFullDto;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.main_svc.event.dto.NewEventDto;
import ru.practicum.main_svc.event.dto.UpdateEventUserRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto dto) {
        log.info("POST users/{userId}/events: create - by id - {}, dto - {}", userId, dto);
        return eventService.createByUser(userId, dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET users/{userId}/events: getAllByUserId - by id - {}", userId);
        return eventService.getAllByUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("GET users/{userId}/events/id: getById - by userId - {}, eventId - {}", userId, eventId);
        return eventService.getByUser(userId, eventId);
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventUserRequest dto) {
        log.info("PATCH users/{userId}/events/id: update - by userId - {}, eventId - {}, dto - {}", userId, eventId, dto);
        return eventService.updateByUser(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllEventsRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        log.info("GET /users/{userId}/events/{eventId}/requests - getAllEventsRequest - userId - {}, eventId - {}",
                userId, eventId);
        return eventService.getEventsRequestByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest dto) {
        log.info("PATCH /users/{userId}/events/{eventId}/requests - updateRequestStatus - userId - {}, eventId - {}",
                userId, eventId);
        return eventService.updateEventRequestByUser(userId, eventId, dto);
    }
}
