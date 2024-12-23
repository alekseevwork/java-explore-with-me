package ru.practicum.stats_server.stats;

import ru.practicum.stats_dto.EndpointsHitDto;
import ru.practicum.stats_dto.ViewStatsDto;
import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.util.List;

public class StatsMapper {

    public static EndpointsHit toEndpointsHit(EndpointsHitDto dto) {
        if (dto == null) {
            return null;
        }
        return new EndpointsHit(
                dto.getId(),
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }

    public static ViewStatsDto toViewStatsDto(final ViewStats viewStats) {
        if (viewStats == null) {
            return null;
        }
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public static List<ViewStatsDto> toViewStatsDto(final List<ViewStats> viewStats) {
        if (viewStats == null) {
            return null;
        }
        return viewStats.stream()
                .map(StatsMapper::toViewStatsDto)
                .toList();
    }
}
