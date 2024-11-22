package ru.practicum.main_svc.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findFirstByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(Long initiatorId, PageRequest pageRequest);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findAllByIdIn(List<Long> ids);

}
