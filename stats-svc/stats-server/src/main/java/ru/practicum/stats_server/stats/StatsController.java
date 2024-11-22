package ru.practicum.stats_server.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats_dto.EndpointsHitDto;
import ru.practicum.stats_dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/")
public class StatsController {

    public final StatsService statsService;

    public StatsController(StatsService stateService) {
        this.statsService = stateService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndpointHit(@RequestBody EndpointsHitDto endpointsHitDto) {
        log.info("POST /hit: create endpoint - {}", endpointsHitDto);
        statsService.create(StatsMapper.toEndpointsHit(endpointsHitDto));
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getViewStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        log.info("GET /stats: getViewStats");
        log.info("start: {}", start);
        log.info("end: {}", end);
        log.info("uris: {}", uris);
        log.info("unique: {}", unique);
        return StatsMapper.toViewStatsDto(statsService.getViewStats(start, end, uris, unique));
    }
}
