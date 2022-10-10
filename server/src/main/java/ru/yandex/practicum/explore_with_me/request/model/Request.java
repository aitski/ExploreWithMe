package ru.yandex.practicum.explore_with_me.request.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

@Table(name = "requests", schema = "public")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name="created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester")
    private User requester;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        return id != null && id.equals(((Request) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
