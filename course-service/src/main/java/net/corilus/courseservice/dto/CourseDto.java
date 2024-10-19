package net.corilus.courseservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.courseservice.entity.Level;

import net.corilus.courseservice.enums.Language;
import org.immutables.value.Value;
import org.springframework.web.multipart.MultipartFile;


@Value.Immutable
@JsonDeserialize(as = ImmutableCourseDto.class)

public interface CourseDto {
      String getTitle();
      String getOwner();
      float getPrice();
      String getDescription();
      String getSpeciality();
      Level getLevel();
      Language getLanguage();
      MultipartFile getVideoFile();
      MultipartFile getImageFile();

}
