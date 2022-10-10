package ru.yandex.practicum.explore_with_me.comment.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@ToString
@Table(name = "bad_words", schema = "public")
public class BadWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;

    @Column(name="word")
    @Size(max = 20)
    String word;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BadWord)) return false;
        return id != null && id.equals(((BadWord) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
