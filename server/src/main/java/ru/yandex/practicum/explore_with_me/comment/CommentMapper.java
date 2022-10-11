package ru.yandex.practicum.explore_with_me.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.comment.model.Comment;
import ru.yandex.practicum.explore_with_me.comment.model.CommentDtoIn;
import ru.yandex.practicum.explore_with_me.comment.model.CommentDtoOut;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public CommentDtoOut convertToDto (Comment comment){
            return new CommentDtoOut(
                    comment.getId(),
                    comment.getText(),
                    comment.getEvent().getId(),
                    comment.getAuthor().getId(),
                    comment.getCreated().format(formatter)
            );
        }

        public Comment convertFromDto (CommentDtoIn commentDto){

            Comment comment = new Comment();
            comment.setText(commentDto.getText());
            return comment;
        }


}
