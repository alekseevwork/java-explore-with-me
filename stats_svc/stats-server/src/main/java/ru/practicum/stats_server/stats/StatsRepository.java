package ru.practicum.stats_server.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointsHit, Long> {

    @Query("SELECT new ru.practicum.stats_server.state.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointsHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end" +
            " GROUP BY h.app, h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> getViewStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats_server.state.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointsHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> getViewStatsUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats_server.state.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointsHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats_server.state.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointsHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> getViewStatsInUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
