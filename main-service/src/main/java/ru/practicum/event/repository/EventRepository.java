package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategoryId(Long categoryId);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(Set<Long> events);

    Optional<Event> findByIdAndStateIs(Long eventId, EventState published);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}