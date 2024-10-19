package net.corilus.courseservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.courseservice.entity.Slide;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableLessonDto.class)

public interface LessonDto {
    String getTitle();
    List<SlideDto> getSlides();
    String getContainername();



}
