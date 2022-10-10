package ru.yandex.practicum.explore_with_me.event.model.Event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.explore_with_me.category.model.Category;
import ru.yandex.practicum.explore_with_me.event.model.Location;
import ru.yandex.practicum.explore_with_me.event.model.State;
import ru.yandex.practicum.explore_with_me.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    //Краткое описание события
    @Size(max = 2000, min = 20)
    @Column(name = "annotation")
    private String annotation;

    //категории к которой относится событие
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //Количество одобренных заявок на участие в данном событии
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    //Полное описание события
    @Size(max = 7000, min = 20)
    @Column(name = "description")
    private String description;

    //Дата и время на которые намечено событие.
    //Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @Column(name = "event_date")
    private LocalDateTime dateOfEvent;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;

    //Широта и долгота места проведения события
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    //Нужно ли оплачивать участие в событии
    @Column(name = "paid")
    private Boolean paid;

    //Ограничение на количество участников.Значение 0 означает отсутствие ограничения
    @Column(name = "participant_limit")
    private Integer participantLimit;

    //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    //Нужна ли пре-модерация заявок на участие.
    //Если true,то все заявки будут ожидать подтверждения инициатором события.
    //Если false-то будут подтверждаться автоматически.
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    //Список состояний жизненного цикла события
    @Enumerated(EnumType.STRING)
    private State state;

    //Заголовок события
    @Size(max = 120, min = 3)
    @Column(name = "title")
    private String title;

    //Количество просмотрев события
    @Column(name = "views")
    private Integer views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        return id != null && id.equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}