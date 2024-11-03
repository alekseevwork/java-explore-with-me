package ru.practicum.stats_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStatsDto {

    String app;
    String uri;
    Long hits;
}
