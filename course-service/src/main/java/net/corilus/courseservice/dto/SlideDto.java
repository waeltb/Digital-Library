package net.corilus.courseservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableSlideDto.class)

public interface SlideDto {
    String getTitle();
    int getOrder();
    String getPathvideo() ;

}
