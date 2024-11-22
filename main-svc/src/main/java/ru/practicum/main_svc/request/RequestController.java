package ru.practicum.main_svc.request;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users/{userId}/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        log.info("POST /users/{userId}/requests - create - userId - {}, eventId - {}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(
            @PathVariable @Positive Long userId) {
        log.info("GET /users/{userId}/requests - getAll - userId - {}", userId);
        return requestService.getAllByUserId(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancel(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId) {
        log.info("PATCH /users/{userId}/requests/{requestId}/cancel - cancel - userId - {}", userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
