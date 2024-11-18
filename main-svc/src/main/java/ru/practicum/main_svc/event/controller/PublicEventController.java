package ru.practicum.main_svc.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.event.EventService;
import ru.practicum.main_svc.event.dto.EventFullDto;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.EndpointsHitDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {

    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest request) {
        statsClient.create(EndpointsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("main-svc")
                .timestamp(LocalDateTime.now())
                .build());
        log.info("GET /events: getById - by id - {}", eventId);
        return eventService.getById(eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getByParams(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) @Positive List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size,
            HttpServletRequest request) {

        statsClient.create(EndpointsHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("main-svc")
                .timestamp(LocalDateTime.now())
                .build());
        log.info("GET /events: getByParams - by text - {}, categories - {}, paid - {}", text, categories, paid);
        return eventService.getAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }
}
