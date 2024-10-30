package ru.practicum.stats_server.state;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStats {

    String app;
    String uri;
    int hits;
}
