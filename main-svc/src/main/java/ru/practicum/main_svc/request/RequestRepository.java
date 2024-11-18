package ru.practicum.main_svc.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    @Query("SELECT r FROM Request r JOIN FETCH r.event e " +
            "WHERE e.initiator.id = :initiatorId AND e.id = :eventId")
    List<Request> findAllByInitiatorIdAndEventId(Long initiatorId, Long eventId);

    @Query("SELECT r FROM Request r WHERE r.id IN :requestIds AND r.event.id = :eventId")
    List<Request> findAllByIdsAndEventId(List<Long> requestIds, Long eventId);

//    @Query("SELECT COUNT r.id FROM Request r WHERE r.event.id = :eventId AND r.status = :status")
//    Long findCountByEventIdAndStatus(Long eventId, RequestStatus status);

    long countAllByEventIdAndStatusIs(long eventId, RequestStatus status);

    Optional<Request> findByRequesterIdAndId(Long requesterId, Long id);
}
