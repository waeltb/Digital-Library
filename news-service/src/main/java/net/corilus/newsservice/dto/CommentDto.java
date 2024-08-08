package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Date;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as =ImmutableCommentDto.class)

public interface CommentDto {
    String getDescription();
    Optional<Integer> getAuthor();
    Optional<Date> getCreationdate();
}
