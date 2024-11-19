package ru.practicum.main_svc.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findFirstByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(Long initiatorId, PageRequest pageRequest);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);

//    @Query("SELECT e FROM Event e WHERE e.publishedOn = :onlyAvailable")
//    List<Event> findAllByPublic(Boolean onlyAvailable, PageRequest pageRequest);

    @Query("SELECT e FROM Event e WHERE e.id IN :ids")
    List<Event> findAllByIds(List<Long> ids);

}
