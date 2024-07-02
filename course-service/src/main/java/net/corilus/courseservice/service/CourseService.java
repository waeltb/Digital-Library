package net.corilus.courseservice.service;

import net.corilus.courseservice.entity.Course;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
    String createCourse( String title,
                         String description,
                         MultipartFile image);
}
