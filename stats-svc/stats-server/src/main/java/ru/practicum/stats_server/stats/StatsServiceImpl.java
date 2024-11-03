package ru.practicum.stats_server.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_server.stats.error.exception.ValidationException;
import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository stateRepository;


    @Override
    @Transactional
    public void create(EndpointsHit state) {
        log.info("Used StatsService.create for - {}", state);
        stateRepository.save(state);
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Used StatsService.getViewStats when start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        if (end.isBefore(start) || end.equals(start)) {
            log.debug("Dates not valid.");
            throw new ValidationException("Start must be before End and they must not be equal.");
        }

        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return stateRepository.getViewStatsUnique(start, end);
            } else {
                return stateRepository.getViewStatsUniqueInUris(start, end, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                return stateRepository.getViewStats(start, end);
            } else {
                return stateRepository.getViewStatsInUris(start, end, uris);
            }
        }
    }
}
