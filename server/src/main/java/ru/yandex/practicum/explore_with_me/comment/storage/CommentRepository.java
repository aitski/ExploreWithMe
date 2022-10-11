package ru.yandex.practicum.explore_with_me.comment.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;

import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByCreatedIsAfterAndCreatedIsBefore (
            LocalDateTime start, LocalDateTime end, Pageable pageable);

}