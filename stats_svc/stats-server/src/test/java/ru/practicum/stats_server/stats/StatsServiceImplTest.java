package ru.practicum.stats_server.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.stats_server.stats.error.exception.ValidationException;
import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatsServiceImplTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_ShouldSaveEndpointsHit() {
        EndpointsHit endpointsHit = new EndpointsHit();
        // Заполните необходимые поля для endpointsHit

        statsService.create(endpointsHit);

        verify(statsRepository, times(1)).save(endpointsHit);
    }

    @Test
    public void getViewStats_ShouldReturnStats_WhenUniqueIsFalse() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = Collections.singletonList("/some-uri");
        List<ViewStats> expectedStats = List.of(new ViewStats("app", "/some-uri", 1L));

        when(statsRepository.getViewStatsInUris(eq(start), eq(end), eq(uris)))
                .thenReturn(expectedStats);
        List<ViewStats> actualStats = statsService.getViewStats(start, end, uris, false);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStatsInUris(start, end, uris);
    }

    @Test
    public void getViewStats_ShouldReturnStats_WhenUniqueIsTrue() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = Collections.singletonList("/some-uri");
        List<ViewStats> expectedStats = List.of(new ViewStats()); // Заполните необходимыми данными

        when(statsRepository.getViewStatsUniqueInUris(start, end, uris)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, uris, true);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStatsUniqueInUris(start, end, uris);
    }

    @Test
    public void getViewStats_ShouldThrowValidationException_WhenEndIsBeforeStart() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusDays(1);
        List<String> uris = Collections.singletonList("/some-uri");

        Exception exception = assertThrows(ValidationException.class, () ->
                statsService.getViewStats(start, end, uris, false));

        assertEquals("Start must be before End and they must not be equal.", exception.getMessage());
    }

    @Test
    public void getViewStats_ShouldThrowValidationException_WhenEndIsEqualToStart() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start;
        List<String> uris = Collections.singletonList("/some-uri");

        ValidationException exception = assertThrows(ValidationException.class, () ->
                statsService.getViewStats(start, end, uris, false));

        assertEquals("Start must be before End and they must not be equal.", exception.getMessage());
    }

    @Test
    public void getViewStats_ShouldReturnStats_WhenUrisIsNullAndUniqueIsTrue() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<ViewStats> expectedStats = List.of(new ViewStats()); // Заполните необходимыми данными

        when(statsRepository.getViewStatsUnique(start, end)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, null, true);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStatsUnique(start, end);
    }

    @Test
    public void getViewStats_ShouldReturnStats_WhenUrisIsEmptyAndUniqueIsFalse() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = Collections.emptyList();
        List<ViewStats> expectedStats = List.of(new ViewStats());

        when(statsRepository.getViewStats(start, end)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, uris, false);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStats(start, end);
    }

    @Test
    public void getViewStats_ShouldReturnUniqueStats_WhenUrisIsNull() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<ViewStats> expectedStats = List.of(new ViewStats()); // Заполните необходимыми данными

        when(statsRepository.getViewStatsUnique(start, end)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, null, true);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStatsUnique(start, end);
    }

    @Test
    public void getViewStats_ShouldReturnUniqueStats_WhenUrisIsEmpty() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = Collections.emptyList();
        List<ViewStats> expectedStats = List.of(new ViewStats()); // Заполните необходимыми данными

        when(statsRepository.getViewStatsUnique(start, end)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, uris, true);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStatsUnique(start, end);
    }

    @Test
    public void getViewStats_ShouldReturnStats_WhenUrisIsNullAndUniqueIsFalse() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<ViewStats> expectedStats = List.of(new ViewStats()); // Заполните необходимыми данными

        when(statsRepository.getViewStats(start, end)).thenReturn(expectedStats);

        List<ViewStats> actualStats = statsService.getViewStats(start, end, null, false);

        assertEquals(expectedStats, actualStats);
        verify(statsRepository, times(1)).getViewStats(start, end);
    }
}