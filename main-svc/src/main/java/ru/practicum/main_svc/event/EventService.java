package ru.practicum.main_svc.event;

import ru.practicum.main_svc.event.dto.EventFullDto;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.main_svc.event.dto.NewEventDto;
import ru.practicum.main_svc.event.dto.UpdateEventAdminRequest;
import ru.practicum.main_svc.event.dto.UpdateEventUserRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getAllByUser(Long userId, int from, int size);

    EventFullDto createByUser(Long userId, NewEventDto dto);

    EventFullDto getByUser(Long userId, Long eventId);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest request);


    List<EventFullDto> getAllByAdmin(List<Long> users, List<String> states, List<Long> categories,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest request);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getById(Long eventId);

    Long getViews(Long eventId, LocalDateTime start);

    List<ParticipationRequestDto> getEventsRequestByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestByUser(
            Long userId, Long eventId, EventRequestStatusUpdateRequest statusUpdateRequest);

    long getConfirmedRequests(Long eventId);
}
