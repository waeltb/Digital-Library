package net.corilus.courseservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.courseservice.entity.Level;

import org.immutables.value.Value;


@Value.Immutable
@JsonDeserialize(as = ImmutableCourseDto.class)

public interface CourseDto {
      String getTitle();
      float getPrice();
      String getDescription();
      String getDescriptiveVideo();
      String getImage();
      String getSpeciality();
      Level getLevel();

}
