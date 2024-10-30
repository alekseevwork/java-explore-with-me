package ru.practicum.stats_server.state;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StateRepository extends JpaRepository<EndpointsHit, Long> {

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) AS hits FROM EndpointsHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end ORDER BY hits DESC")
    List<ViewStats> getViewStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) AS hits FROM EndpointsHit AS h " +
            "WHERE h.uri IN :usris AND h.timestamp BETWEEN :start AND :end ORDER BY hits DESC")
    List<ViewStats> getViewStatsUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> usris);

    @Query("SELECT h.app, h.uri, COUNT(h.ip) AS hits FROM EndpointsHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end ORDER BY hits DESC")
    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app, h.uri, COUNT(h.ip) AS hits FROM EndpointsHit AS h " +
            "WHERE h.uri IN :usris AND h.timestamp BETWEEN :start AND :end ORDER BY hits DESC")
    List<ViewStats> getViewStatsInUris(LocalDateTime start, LocalDateTime end, List<String> usris);
}
