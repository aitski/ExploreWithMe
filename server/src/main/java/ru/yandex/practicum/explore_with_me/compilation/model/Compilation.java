package ru.yandex.practicum.explore_with_me.compilation.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString

@Table(name = "compilations", schema = "public")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "comp_events",
            joinColumns = {@JoinColumn(name = "comp_id_event")},
            inverseJoinColumns = {@JoinColumn(name = "event_id_comp")})
    @ToString.Exclude
    private List<Event> events;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compilation)) return false;
        return id != null && id.equals(((Compilation) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
