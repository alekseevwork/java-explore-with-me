package ru.practicum.stats_server.state;

import java.time.LocalDateTime;
import java.util.List;

public interface StateService {

    void create(EndpointsHit responseDto);
    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> usris, boolean unique);
}
