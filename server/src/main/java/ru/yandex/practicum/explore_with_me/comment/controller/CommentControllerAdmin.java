package ru.yandex.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.comment.model.BadWord;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;
import ru.yandex.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("admin/comments")
@RequiredArgsConstructor

public class CommentControllerAdmin {

    private final CommentService commentService;

    //Получение списка комментариев по поиску текста (без учета регистра), по списку пользователей, по списку событий,
    //по дате создания коммента в диапазоне дат. Для админа выводится детальная информация
    @GetMapping
    public List<Comment> getAllAdmin(@RequestParam(required = false) String text,
                                     @RequestParam(required = false) Long[] users,
                                     @RequestParam(required = false) Long[] events,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Positive @RequestParam(defaultValue = "10") int size
    ) {

        return commentService.getAll(text, users, events, rangeStart,
                rangeEnd, from, size);
    }

    //Получение коммента по айди
    @GetMapping("/{commentId}")
    public Comment getByCommentIdAdmin(@PathVariable long commentId) {
        return commentService.getById(commentId);
    }

    //Удаление коммента
    @DeleteMapping("/{commentId}")
    public void deleteAdmin(@PathVariable long commentId) {

        commentService.deleteAdmin(commentId);
    }

    //Получение списка нецензурных слов для авто-модерации комментариев. Доступно только админам.
    @GetMapping("/badwords")
    public List<BadWord> getAllBad() {
        return commentService.getAllBad();
    }

    @GetMapping("/badwords/{id}")
    public BadWord getByIdBad(@PathVariable long id) {
        return commentService.getByIdBad(id);
    }

    @PostMapping("/badwords")
    public BadWord createBad(@RequestBody BadWord badWord) {
        return commentService.createBad(badWord);
    }

    @PutMapping("/badwords/{id}")
    public BadWord updateBad(@PathVariable long id,
                          @RequestBody BadWord badWord) {
        return commentService.updateBad(id, badWord);
    }

    @DeleteMapping("/badwords/{id}")
    public void deleteBad(@PathVariable long id) {
        commentService.deleteBad(id);
    }
}
