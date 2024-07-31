package net.corilus.newsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Date;

@Value.Immutable
@JsonDeserialize(as =ImmutablePublicationDto.class)
public interface PublicationDto {
    String getTitle();
    String getDescription();
    String getImage();
    String getSpeciality();
    String getContainername();
}
