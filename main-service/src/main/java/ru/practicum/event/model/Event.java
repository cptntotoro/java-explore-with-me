package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cat_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L;

    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator")
    private User initiator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location")
    private Location location;

    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit = 0L;

    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Size(min = 3, max = 120)
    private String title;

    private Long views = 0L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(annotation, event.annotation) && Objects.equals(category, event.category) && Objects.equals(confirmedRequests, event.confirmedRequests) && Objects.equals(createdOn, event.createdOn) && Objects.equals(description, event.description) && Objects.equals(eventDate, event.eventDate) && Objects.equals(initiator, event.initiator) && Objects.equals(location, event.location) && Objects.equals(paid, event.paid) && Objects.equals(participantLimit, event.participantLimit) && Objects.equals(publishedOn, event.publishedOn) && Objects.equals(requestModeration, event.requestModeration) && state == event.state && Objects.equals(title, event.title) && Objects.equals(views, event.views) && Objects.equals(compilations, event.compilations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, category, confirmedRequests, createdOn, description, eventDate, initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title, views, compilations);
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "comp_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations = new ArrayList<>();
}