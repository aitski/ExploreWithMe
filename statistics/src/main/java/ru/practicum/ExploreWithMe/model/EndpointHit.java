package ru.practicum.ExploreWithMe.model;

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
    Long id;

    @Column(name="app")
    String app;

    @Column(name="uri")
    String uri;

    @Column(name="ip")
    String ip;

    @Column(name="time_stamp")
    LocalDateTime timestamp;
}
