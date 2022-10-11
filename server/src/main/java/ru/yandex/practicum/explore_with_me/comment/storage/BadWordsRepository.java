package ru.yandex.practicum.explore_with_me.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.comment.model.BadWord;

public interface BadWordsRepository extends JpaRepository<BadWord, Long> {

}