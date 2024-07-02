package net.corilus.userservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableSpecialityDto.class)
public interface SpecialityDto {
    String getName();
}
