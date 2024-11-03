package ru.practicum.stats_server.stats;

import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void create(EndpointsHit responseDto);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> usris, boolean unique);
}
