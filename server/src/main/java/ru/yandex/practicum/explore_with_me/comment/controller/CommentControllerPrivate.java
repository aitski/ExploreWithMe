package ru.yandex.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.comment.CommentMapper;
import ru.yandex.practicum.explore_with_me.comment.model.CommentDtoIn;
import ru.yandex.practicum.explore_with_me.comment.model.CommentDtoOut;
import ru.yandex.practicum.explore_with_me.comment.service.CommentService;

@RestController
@RequestMapping("users/{userId}/comments")
@RequiredArgsConstructor

public class CommentControllerPrivate {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    //Создание пользователем коммента на событие
    @PostMapping("/events/{eventId}")
    public CommentDtoOut create(@RequestBody CommentDtoIn comment,
                                @PathVariable long userId,
                                @PathVariable long eventId) {

        return commentMapper.convertToDto(
                commentService.create(userId, eventId,commentMapper.convertFromDto(comment))
        );
    }
    //Редактирование пользователем текста своего коммента
    @PatchMapping("{commentId}")
    public CommentDtoOut patch(@RequestBody CommentDtoIn comment,
                               @PathVariable long commentId,
                               @PathVariable long userId) {

        return commentMapper.convertToDto(
                commentService.patch(userId, commentId, commentMapper.convertFromDto(comment))
        );
    }
    //Удаление пользователем своего коммента
    @DeleteMapping("/{commentId}")
    public void deletePrivate(@PathVariable long userId,
                            @PathVariable long commentId) {

        commentService.deletePrivate(userId, commentId);
    }

}
