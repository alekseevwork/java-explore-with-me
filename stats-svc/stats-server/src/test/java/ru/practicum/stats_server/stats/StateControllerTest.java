package ru.practicum.stats_server.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats_dto.EndpointsHitDto;
import ru.practicum.stats_dto.ViewStatsDto;
import ru.practicum.stats_server.stats.error.exception.ValidationException;
import ru.practicum.stats_server.stats.model.EndpointsHit;
import ru.practicum.stats_server.stats.model.ViewStats;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private StatsMapper stateMapper;

    private EndpointsHitDto endpointsHitDto;
    private EndpointsHit endpointsHit;

    private ViewStatsDto viewStatsDto;
    private ViewStats viewStat;

    @BeforeEach
    public void before() {
        endpointsHitDto = EndpointsHitDto.builder()
                .app("app")
                .uri("http://example.com/api/v1/users")
                .ip("1")
                .timestamp(LocalDateTime.now())
                .build();
        endpointsHit = StatsMapper.toEndpointsHit(endpointsHitDto);

        viewStat = ViewStats.builder()
                .uri("http://example.com/api/v1/users")
                .app("app")
                .hits(2L)
                .build();
        viewStatsDto = StatsMapper.toViewStatsDto(viewStat);
    }

    @Test
    public void createEndpointHit_shouldReturnOk() throws Exception {
        doNothing().when(statsService).create(endpointsHit);

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(endpointsHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getViewStats_shouldReturnOk() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 26, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        List<String> uris = List.of("http://example.com/api/v1/users", "http://example.com/api/v1/posts");
        List<ViewStats> viewStats = List.of(viewStat);

        when(statsService.getViewStats(eq(start), eq(end), eq(uris), eq(false))).thenReturn(viewStats);

        mockMvc.perform(get("/stats")
                        .param("start", start.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("end", end.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("uris", String.join(",", uris))
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].app", is(viewStatsDto.getApp()), String.class))
                .andExpect(jsonPath("$[0].uri", is(viewStatsDto.getUri()), String.class))
                .andExpect(jsonPath("$[0].hits", is(viewStatsDto.getHits()), Long.class));
    }

    @Test
    public void getViewStats_withNoUris_shouldReturnOk() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 26, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        List<ViewStats> viewStats = List.of(viewStat);

        when(statsService.getViewStats(eq(start), eq(end), eq(null), eq(false))).thenReturn(viewStats);

        mockMvc.perform(get("/stats")
                        .param("start", start.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("end", end.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("unique", "false"))
                .andExpect(status().isOk());
    }

    @Test
    public void getViewStats_withUniqueTrue_shouldReturnOk() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 26, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        List<String> uris = List.of("http://example.com/api/v1/users", "http://example.com/api/v1/posts");
        List<ViewStats> viewStats = List.of(viewStat);

        when(statsService.getViewStats(any(LocalDateTime.class), any(LocalDateTime.class), anyList(), anyBoolean()))
                .thenReturn(viewStats);

        mockMvc.perform(get("/stats")
                        .param("start", start.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("end", end.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("uris", String.join(",", uris))
                        .param("unique", "true"))
                .andExpect(status().isOk());
    }

    @Test
    public void getViewStats_withDateNotValid_shouldThrowsDateValidationException() throws Exception {
        when(statsService.getViewStats(any(LocalDateTime.class), any(LocalDateTime.class), anyList(), anyBoolean()))
                .thenThrow(new ValidationException("Start must be before End and they must not be equal."));

        List<String> uris = List.of("http://example.com/api/v1/users");
        mockMvc.perform(get("/stats")
                        .param("start", String.valueOf(LocalDateTime.now()))
                        .param("end", String.valueOf(LocalDateTime.now()))
                        .param("uris", String.join(",", uris))
                        .param("unique", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",
                        is("Start must be before End and they must not be equal."),
                        String.class));
    }
}