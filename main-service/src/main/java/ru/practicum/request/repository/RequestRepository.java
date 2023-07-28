package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByIdInAndAndEventId(Iterable<Long> ids, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);

}
