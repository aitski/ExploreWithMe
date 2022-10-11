package ru.yandex.practicum.explore_with_me.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.comment.model.BadWord;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;
import ru.yandex.practicum.explore_with_me.comment.storage.BadWordsRepository;
import ru.yandex.practicum.explore_with_me.comment.storage.CommentRepository;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.service.EventService;
import ru.yandex.practicum.explore_with_me.exception.exceptions.NotFoundException;
import ru.yandex.practicum.explore_with_me.exception.exceptions.PrivilegeException;
import ru.yandex.practicum.explore_with_me.exception.exceptions.ValidationException;
import ru.yandex.practicum.explore_with_me.user.model.User;
import ru.yandex.practicum.explore_with_me.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;
    private final BadWordsRepository badWordsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Comment> getAll(String text, Long[] users, Long[] events,
                                String rangeStart, String rangeEnd,
                                int from, int size) {

        List<Comment> list;
        Pageable pageRequest = PageRequest.of(from / size, size);

        //если в запросе не указан диапазон дат [rangeStart-rangeEnd],
        // то выгружаются комменты за все время
        if (rangeEnd == null || rangeStart == null) {
            list = commentRepository.findAll(pageRequest).getContent();
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
            list = commentRepository.findAllByCreatedIsAfterAndCreatedIsBefore(
                    start, end, pageRequest).getContent();
        }

        if (users != null) {
            List<Long> us = Arrays.asList(users);
            list = list.stream().filter(
                            comment -> us.contains(comment.getAuthor().getId()))
                    .collect(Collectors.toList());
        }

        if (events != null) {
            List<Long> ev = Arrays.asList(events);
            list = list.stream().filter(
                            comment -> ev.contains(comment.getEvent().getId()))
                    .collect(Collectors.toList());
        }

        //текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        if (text != null) {
            list = list.stream().filter
                            (comment -> comment.getText().toLowerCase().
                                    contains(text.toLowerCase())).
                    collect(Collectors.toList());
        }
        log.debug("list of comments returned: text search {}, users list {}, " +
                "events list {}, \n" +
                "start date {}, end date {}, \n" +
                "from page {}, number of pages {}: " +
                "{}", text, users, events, rangeStart, rangeEnd, from, size, list);
        return list;
    }

    @Override
    public Comment getById(long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment", commentId));
        log.debug("Comment returned {}", comment);
        return comment;
    }


    @Override
    public Comment create(long userId, long eventId, Comment comment) {

        User author = userService.getById(userId);
        Event event = eventService.getByEventId(eventId);
        validateBadWords(comment);
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        Comment newComment = commentRepository.save(comment);
        log.debug("Comment created {}", newComment);
        return newComment;
    }

    @Override
    public Comment patch(long userId, long commentId, Comment comment) {

        //comment validation
        Comment commentUpd = getById(commentId);
        validateOwnerShip(userId, commentUpd);
        validateBadWords(comment);
        commentUpd.setText(comment.getText());
        commentRepository.save(commentUpd);
        log.debug("Comment created {}", commentUpd);
        return commentUpd;
    }

    @Override
    public void deletePrivate(long userId, long commentId) {

        //comment validation
        Comment comment = getById(commentId);
        validateOwnerShip(userId, comment);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteAdmin(long commentId) {

        //validate comment
        Comment comment = getById(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public List<BadWord> getAllBad() {
        return badWordsRepository.findAll();
    }

    @Override
    public BadWord getByIdBad(long id) {
        return badWordsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BadWord", id));
    }

    @Override
    public BadWord createBad(BadWord badWord) {
        return badWordsRepository.save(badWord);
    }

    @Override
    public BadWord updateBad(long id, BadWord badWord) {
        BadWord badWord1 = getByIdBad(id);
        return badWordsRepository.save(badWord1);
    }


    @Override
    public void deleteBad(long id) {
        badWordsRepository.deleteById(id);
    }

    private void validateBadWords(Comment comment) {
        //проверка, содержит ли коммент какое-либо нецензурное слово (без учета регистра)
        if (getAllBad().stream()
                .map(badWord -> badWord.getWord().toLowerCase())
                .anyMatch(word -> comment.getText().toLowerCase().contains(word))) {
            ValidationException e = new ValidationException("Комментарий содержит нецензурную лексику");
            log.error(e.getMessage());
            throw e;
        }
    }

    private void validateOwnerShip(long userId, Comment comment) {

        //user validation
        userService.getById(userId);

        //Validate ownership of comment
        if (!comment.getAuthor().getId().equals(userId)) {

            PrivilegeException e = new PrivilegeException("Нелья менять чужой комментарий");
            log.error(e.getMessage());
            throw e;
        }
    }
}