package ru.practicum.main_svc.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.category.Category;
import ru.practicum.main_svc.category.CategoryRepository;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.error.exception.RequestConflictException;
import ru.practicum.main_svc.event.dto.EventFullDto;
import ru.practicum.main_svc.event.dto.EventShortDto;
import ru.practicum.main_svc.event.dto.NewEventDto;
import ru.practicum.main_svc.event.dto.UpdateEventAdminRequest;
import ru.practicum.main_svc.event.dto.UpdateEventUserRequest;
import ru.practicum.main_svc.request.QRequest;
import ru.practicum.main_svc.request.Request;
import ru.practicum.main_svc.request.RequestMapper;
import ru.practicum.main_svc.request.RequestRepository;
import ru.practicum.main_svc.request.RequestStatus;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_svc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_svc.request.dto.ParticipationRequestDto;
import ru.practicum.main_svc.user.User;
import ru.practicum.main_svc.user.UserRepository;
import ru.practicum.stats_client.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImp implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;


    @Override
    public List<EventShortDto> getAllByUser(Long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size)).stream()
                .map(event -> EventMapper.toShortDto(event, this)).toList();
    }

    @Transactional
    @Override
    public EventFullDto createByUser(Long userId, NewEventDto dto) {
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (LocalDateTime.now()
                .isAfter(LocalDateTime.parse(dto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))) {
            throw new ValidationException("Event date is past");
        }


        if (dto.getPaid() == null) {
            dto.setPaid(false);
        }
        if (dto.getParticipantLimit() == null) {
            dto.setParticipantLimit(0);
        }
        if (dto.getRequestModeration() == null) {
            dto.setRequestModeration(true);
        }

        Event event = EventMapper.toEvent(dto);

        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setState(State.PENDING);

        EventFullDto eventFullDto = EventMapper.toDto(eventRepository.save(event), this);
        eventFullDto.setViews(getViews(eventFullDto.getId(), eventFullDto.getCreatedOn()));

        return eventFullDto;
    }

    @Override
    public EventFullDto getByUser(Long userId, Long eventId) {
        EventFullDto eventFullDto = EventMapper.toDto(eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event not found")), this);

        eventFullDto.setViews(getViews(eventFullDto.getId(), eventFullDto.getCreatedOn()));

        return eventFullDto;
    }

    @Transactional
    @Override
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (request.getParticipantLimit() != null && request.getParticipantLimit() < 0) {
            throw new ValidationException("Participant Limit cannot be less than 0");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new RequestConflictException("No rights to change events with PUBLISHED status");
        }
        if (request.getEventDate() != null && LocalDateTime.now().isAfter(request.getEventDate())) {
            throw new ValidationException("Event date is past");
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category by id: " + request.getCategory() +
                            " not found")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getStateAction() != null) {
            StateAction stateAction = StateAction.from(request.getStateAction())
                    .orElseThrow(() -> new RequestConflictException("StateAction by - " + request.getStateAction() +
                            "invalid"));
            if (stateAction.equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else if (stateAction.equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        return EventMapper.toDto(eventRepository.save(event), this);
    }

    @Override
    public List<EventFullDto> getAllByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        final JPAQuery<Event> query = new JPAQuery<>(entityManager);
        QEvent event = QEvent.event;
        query.from(event);

        if (users != null && !users.isEmpty()) {
            query.where(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            query.where(event.state.in(states.stream().map(State::valueOf).toList()));
        }
        if (categories != null && !categories.isEmpty()) {
            query.where(event.category.id.in(categories));
        }
        if (rangeStart != null) {
            query.where(event.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            query.where(event.eventDate.loe(rangeEnd));
        }
        List<Event> events = query.offset(from).limit(size).fetch();

        return events.stream()
                .map(e -> EventMapper.toDto(e, this))
                .toList();
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event by id - " + eventId + " not found"));

        if (!event.getState().equals(State.PENDING)) {
            throw new RequestConflictException("Event state not PENDING");
        }
        if (request.getEventDate() != null && LocalDateTime.now().isAfter(request.getEventDate())) {
            throw new ValidationException("Event date is past");
        }

        if (request.getStateAction() != null) {
            StateAction stateAction = StateAction.from(request.getStateAction())
                    .orElseThrow(() -> new RequestConflictException("StateAction not correct"));

            if (stateAction.equals(StateAction.PUBLISH_EVENT)) {
                event.setPublishedOn(LocalDateTime.now());
                event.setState(State.PUBLISHED);
            } else if (stateAction.equals(StateAction.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            event.setCategory(categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category by id: " + request.getCategory() +
                            " not found")));
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        return EventMapper.toDto(eventRepository.save(event), this);
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        final JPAQuery<Event> query = new JPAQuery<>(entityManager);
        QEvent event = QEvent.event;
        QRequest request = QRequest.request;
        query.from(event);
        query.where(event.state.eq(State.PUBLISHED));

        if (text != null && !text.isEmpty()) {
            query.where(event.annotation.containsIgnoreCase(text)
                    .or(event.description.containsIgnoreCase(text)));
        }
        if (categories != null && !categories.isEmpty()) {
            query.where(event.category.id.in(categories));
        }
        if (paid != null) {
            query.where(event.paid.eq(paid));
        }
        if (rangeStart != null) {
            query.where(event.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            query.where(event.eventDate.loe(rangeEnd));
        }
        if (rangeStart == null && rangeEnd == null) {
            query.where(event.eventDate.goe(LocalDateTime.now()));
        }

        if (rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Start date cannot be after end");
        }

        if (onlyAvailable) {
            query.leftJoin(request).on(request.event.eq(event))
                    .groupBy(event.id)
                    .having(request.id.count().gt(0))
                    .having(request.event.id.count().loe(event.participantLimit));
        }
        List<Event> events = query.offset(from).limit(size).fetch();
        List<EventShortDto> eventsDtoList = events.stream()
                .map(e -> EventMapper.toShortDto(e, this))
                .collect(Collectors.toList());

        if (sort != null) {
            if (EventSort.valueOf(sort).equals(EventSort.EVENT_DATE)) {
                eventsDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
            } else if (EventSort.valueOf(sort).equals(EventSort.VIEWS)) {
                eventsDtoList.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return eventsDtoList;
    }

    @Override
    public EventFullDto getById(Long eventId) {
        EventFullDto eventFullDto = EventMapper.toDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found")), this);

        if (eventFullDto.getState().equals(State.PENDING)) {
            throw new NotFoundException("Event not yet published");
        }

        eventFullDto.setViews(getViews(eventFullDto.getId(), eventFullDto.getCreatedOn()));

        return eventFullDto;
    }

    @Override
    public Long getViews(Long eventId, LocalDateTime start) {
        String uris = "/events/" + eventId;

        ResponseEntity<Object> response = statsClient.getViewStats(
                start.minusDays(1), LocalDateTime.now(),
                List.of(uris), true);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, Object>> statsResponseList = objectMapper.convertValue(
                response.getBody(),
                new TypeReference<>() {
                });
        long views = 0L;
        if (statsResponseList != null && !statsResponseList.isEmpty()) {
            views = ((Number) statsResponseList.getFirst().get("hits")).longValue();
        }

        return views;
    }

    @Override
    public List<ParticipationRequestDto> getEventsRequestByUser(Long userId, Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с таким id: " + eventId + " не найдено"));

        return requestRepository.findAllByInitiatorIdAndEventId(userId, eventId).stream()
                .map(RequestMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateEventRequestByUser(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest statusUpdateRequest) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User by id - " + userId + "  not found."));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new NotFoundException(""));
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new ValidationException("Confirmation of applications is not required");
        }
        List<Long> requestIds = statusUpdateRequest.getRequestIds();
        List<Request> updatesRequests = requestRepository.findAllByIdsAndEventId(requestIds, eventId);

        long confirmedCount = requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED);

        if (confirmedCount >= event.getParticipantLimit()) {
            throw new RequestConflictException("Quantity Participant Limit is full");
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (statusUpdateRequest.getStatus() == RequestStatus.CONFIRMED) {
            for (Request request : updatesRequests) {
                if (request.getStatus() == RequestStatus.PENDING
                        && confirmedCount <= event.getParticipantLimit()) {
                    confirmedRequests.add(setStatusAndSaveAll(request, RequestStatus.CONFIRMED));
                    ++confirmedCount;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(setStatusAndSaveAll(request, RequestStatus.REJECTED));
                }
            }
        } else if (statusUpdateRequest.getStatus() == RequestStatus.REJECTED){
            for (Request request : updatesRequests) {
                if (request.getStatus() == RequestStatus.CONFIRMED) {
                    throw new RequestConflictException("State request invalid");
                }
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(setStatusAndSaveAll(request, RequestStatus.REJECTED));
            }
        } else {
            throw new RequestConflictException("State request invalid");
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private ParticipationRequestDto setStatusAndSaveAll(Request request, RequestStatus status) {
        request.setStatus(status);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public long getConfirmedRequests(Long eventId) {
        return requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED);
    }
}
