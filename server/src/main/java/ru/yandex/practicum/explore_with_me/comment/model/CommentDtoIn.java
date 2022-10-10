package ru.yandex.practicum.explore_with_me.comment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDtoIn {

    private String text;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CommentDtoIn (String text){
        this.text=text;
    }

}
