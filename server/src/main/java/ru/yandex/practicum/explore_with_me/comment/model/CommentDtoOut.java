package ru.yandex.practicum.explore_with_me.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentDtoOut {

    private long id;
    private String text;
    private long eventId;
    private long authorId;
    private String created;
}
