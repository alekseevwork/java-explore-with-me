package ru.practicum.main_svc.request.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.main_svc.request.RequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}