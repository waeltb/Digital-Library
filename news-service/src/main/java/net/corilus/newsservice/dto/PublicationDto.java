package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Date;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as =ImmutablePublicationDto.class)
public interface PublicationDto {
    Optional<Integer> getId();
    Optional<Integer> getAuthor();
    String getTitle();
    String getDescription();
    String getImage();
    String getSpeciality();
    String getContainername();
    Optional<String> getUsername();
}
