package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @NotNull
    private String uri;

    @NotNull
    private String ip;

    @NotNull
    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    public Request(Integer id, String uri, String ip, LocalDateTime timestamp, Integer appId, String name) {
        this.id = id;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
        this.app = new App(appId, name);
    }
}