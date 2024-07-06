package net.corilus.courseservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.courseservice.entity.Level;
import net.corilus.courseservice.entity.Status;
import org.immutables.value.Value;

import java.util.Date;

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
