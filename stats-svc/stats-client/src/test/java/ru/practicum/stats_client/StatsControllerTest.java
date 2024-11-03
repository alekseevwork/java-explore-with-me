package ru.practicum.stats_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.stats_dto.EndpointsHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private StatsClient statsClient;

    private EndpointsHitDto endpointsHitDto;

    @BeforeEach
    public void before() {
        endpointsHitDto = EndpointsHitDto.builder()
                .app("new")
                .uri("/events/new")
                .ip("1")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    public void createEndpointHit_shouldReturnCreated() throws Exception {
        when(statsClient.create(any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(MockMvcRequestBuilders.post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointsHitDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createEndpointHit_shouldReturnBadRequest() throws Exception {
        when(statsClient.create(any())).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        endpointsHitDto.setApp("");
        mockMvc.perform(MockMvcRequestBuilders.post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endpointsHitDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getViewStats_shouldReturnOk() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 26, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        List<String> uris = List.of("http://example.com/api/v1/users", "http://example.com/api/v1/posts");

        when(statsClient.getViewStats(eq(start), eq(end), eq(uris), eq(false)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/stats")
                        .param("start", start.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("end", end.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("uris", String.join(",", uris))
                        .param("unique", "false"))
                .andExpect(status().isOk());
    }

    @Test
    public void getViewStats_withUniqueParam_shouldReturnOk() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 26, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        List<String> uris = List.of("http://example.com/api/v1/users", "http://example.com/api/v1/posts");

        when(statsClient.getViewStats(eq(start), eq(end), eq(uris), eq(true)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.get("/stats")
                        .param("start", start.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("end", end.format(DateTimeFormatter.ISO_DATE_TIME))
                        .param("uris", String.join(",", uris))
                        .param("unique", "true"))
                .andExpect(status().isOk());
    }
}
