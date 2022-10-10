package ru.yandex.practicum.explore_with_me.comment.service;

import ru.yandex.practicum.explore_with_me.comment.model.BadWord;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAll(String text, Long[] users, Long[] events,
                         String rangeStart, String rangeEnd,
                         int from, int size);

    Comment getById(long id);

    Comment create(long userId, long eventId, Comment comment);

    Comment patch(long userId, long commentId, Comment comment);

    void deleteAdmin(long commentId);

    void deletePrivate(long userId, long commentId);

    List<BadWord> getAllBad();

    BadWord getByIdBad(long id);

    BadWord createBad(BadWord badWord);

    BadWord updateBad(long id, BadWord badWord);

    void deleteBad(long id);
}
