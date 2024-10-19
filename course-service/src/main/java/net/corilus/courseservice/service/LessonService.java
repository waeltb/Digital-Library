package net.corilus.courseservice.service;


import net.corilus.courseservice.dto.CourseDto;
import net.corilus.courseservice.dto.LessonDto;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.entity.Lesson;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LessonService {
    void createLesson(Lesson lesson, MultipartFile videoLesson) throws IOException;
    void DeleteLesson(Integer lessonId);
    Lesson convertToEntity(LessonDto lessonDto);
}
