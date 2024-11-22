package ru.practicum.main_svc.request;

import ru.practicum.main_svc.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getAllByUserId(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto update(Long userId, Long requestId);

    ParticipationRequestDto cancelRequest(Long requesterId, Long reqId);
}
