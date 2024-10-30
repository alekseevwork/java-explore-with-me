package ru.practicum.stats_server.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;


    @Override
    @Transactional
    public void create(EndpointsHit state) {
        stateRepository.save(state);
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> usris, boolean unique) {

        if (unique) {
            if (usris.isEmpty()) {
                return stateRepository.getViewStatsUnique(start, end);
            } else {
                return stateRepository.getViewStatsUniqueInUris(start, end, usris);
            }
        } else {
            if (usris.isEmpty() ) {
                return stateRepository.getViewStats(start, end);
            } else {
                return stateRepository.getViewStatsInUris(start, end, usris);
            }
        }
    }
}
