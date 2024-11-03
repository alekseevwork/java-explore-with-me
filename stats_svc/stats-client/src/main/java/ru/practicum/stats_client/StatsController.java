package ru.practicum.stats_client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats_dto.EndpointsHitDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class StatsController {

    public final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createEndpointHit(@Valid @RequestBody EndpointsHitDto endpointsHitDto) {
        return statsClient.create(endpointsHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getViewStats(
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        return statsClient.getViewStats(start, end, uris, unique);
    }
}
