package ru.practicum.explore_with_me.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

@Table(name = "endpoint_hit", schema = "public")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="app")
    private String app;

    @Column(name="uri")
    private String uri;

    @Column(name="ip")
    private String ip;

    @Column(name="time_stamp")
    private LocalDateTime timestamp;
}
