package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
@JsonDeserialize(as =ImmutableCommentDto.class)

public interface CommentDto {
    String getDescription();
}
