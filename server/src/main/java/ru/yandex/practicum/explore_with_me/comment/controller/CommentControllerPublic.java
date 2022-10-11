package ru.yandex.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.comment.CommentMapper;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;
import ru.yandex.practicum.explore_with_me.comment.model.CommentDtoOut;
import ru.yandex.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentControllerPublic {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    //Получение списка комментариев по поиску текста (без учета регистра), по списку пользователей, по списку событий,
    //по дате создания коммента в диапазоне дат. Для публичного доступа выводится краткая информация
    @GetMapping
    public List<CommentDtoOut> getAllPublic(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) Long[] users,
                                            @RequestParam(required = false) Long[] events,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                            @Positive @RequestParam(required = false, defaultValue = "10") int size
    ) {

        List<Comment> list = commentService.getAll(text, users, events, rangeStart,
                rangeEnd, from, size);
        return list.stream()
                .map(commentMapper::convertToDto)
                .collect(Collectors.toList());
    }

    //Получение коммента по айди
    @GetMapping("/{commentId}")
    public CommentDtoOut getByCommentIdPublic(@PathVariable long commentId) {
        return commentMapper.convertToDto(
                commentService.getById(commentId));
    }

}
