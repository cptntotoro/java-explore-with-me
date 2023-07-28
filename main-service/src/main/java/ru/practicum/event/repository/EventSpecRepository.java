package ru.practicum.event.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.EventState;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventSpecRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    static Specification<Event> hasText(String text) {
        return (event, query, critBuilder) -> {
            if (text == null) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                return critBuilder.or(critBuilder.like(critBuilder.lower(event.get("annotation")), text.toLowerCase()),
                        critBuilder.like(critBuilder.lower(event.get("description")), text.toLowerCase()));
            }
        };
    }

    static Specification<Event> hasCategories(List<Long> categories) {
        return (event, query, critBuilder) -> {
            if (categories == null || categories.size() == 0) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                    CriteriaBuilder.In<Long> categoryIds = critBuilder.in(event.get("category"));
                    for (long catId : categories) {
                        categoryIds.value(catId);
                    }
                return categoryIds;
            }
        };
    }

    static Specification<Event> hasUsers(List<Long> users) {
        return (event, query, critBuilder) -> {
            if (users == null || users.size() == 0) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                CriteriaBuilder.In<Long> userIds = critBuilder.in(event.get("initiator"));
                for (long userId : users) {
                    userIds.value(userId);
                }
                return userIds;
            }
        };
    }

    static Specification<Event> hasStates(List<EventState> states) {
        return (event, query, critBuilder) -> {
            if (states == null || states.size() == 0) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                CriteriaBuilder.In<EventState> cbStates = critBuilder.in(event.get("state"));
                for (EventState state : states) {
                    cbStates.value(state);
                }
                return cbStates;
            }
        };
    }

    static Specification<Event> hasRangeStart(LocalDateTime rangeStart) {
        return (event, query, critBuilder) -> {
            if (rangeStart == null) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                return critBuilder.greaterThan(event.get("eventDate"), rangeStart);
            }
        };
    }

    static Specification<Event> hasRangeEnd(LocalDateTime rangeEnd) {
        return (event, query, critBuilder) -> {
            if (rangeEnd == null) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                return critBuilder.lessThan(event.get("eventDate"), rangeEnd);
            }
        };
    }

    static Specification<Event> hasPaid(Boolean paid) {
        return (event, query, critBuilder) -> {
            if (paid == null) {
                return critBuilder.isTrue(critBuilder.literal(true));
            } else {
                return critBuilder.equal(event.get("paid"), paid);
            }
        };
    }

    static Specification<Event> hasAvailable(Boolean onlyAvailable) {
        return (event, query, critBuilder) -> {
            if (onlyAvailable != null && onlyAvailable) {
                return critBuilder.or(critBuilder.le(event.get("confirmedRequests"), event.get("participantLimit")),
                        critBuilder.le(event.get("participantLimit"), 0));
            } else {
                return critBuilder.isTrue(critBuilder.literal(true));
            }
        };
    }
}
