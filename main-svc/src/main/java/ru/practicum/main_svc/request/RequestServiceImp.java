package ru.practicum.main_svc.request;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.error.exception.RequestConflictException;
import ru.practicum.main_svc.event.Event;
import ru.practicum.main_svc.event.EventRepository;
import ru.practicum.main_svc.event.State;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;
import ru.practicum.main_svc.user.User;
import ru.practicum.main_svc.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestServiceImp(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getAllByUserId(Long userId) {
        return requestRepository.findAllByRequesterId(userId).stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {

        Request request = new Request();
        request.setCreated(LocalDateTime.now());

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (requestRepository.findByRequesterIdAndEventId(userId, event.getId()).isPresent()) {
            throw new RequestConflictException("User already this event");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new RequestConflictException("User is initiator event");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RequestConflictException("Event not published");
        }

        long confirmedCount = requestRepository.countAllByEventId(eventId);

        if (event.getParticipantLimit() > 0 && confirmedCount >= event.getParticipantLimit()) {
            throw new RequestConflictException("Quantity Participant Limit is full");
        }

        request.setRequester(user);
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto update(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Not requests by id - " + requestId));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }


    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId ) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User by id: " + userId + " not found"));

        Request request = requestRepository.findByRequesterIdAndId(userId, requestId).orElseThrow(() ->
                new NotFoundException(""));

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = request.getEvent();
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }
}

