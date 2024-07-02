package net.corilus.courseservice.service;

import lombok.AllArgsConstructor;
import net.corilus.courseservice.dto.Container;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    CourseRepository courseRepository ;
    @Autowired
    AzureBlobStorageImpl azureBlobStorage;

    @Override
    public String createCourse( String title,
                                String description,
                                MultipartFile image
                              ) {
        try {
            String imagePath = azureBlobStorage.uploadFile(image);
            Course course = new Course.Builder()
                    .title(title)
                    .description(description)
                    .imagepath(imagePath)
                    .creationdate(new Date())
                    .modificationdate(new Date())
                    .reasonforrefusal(null)
                    .build();
            courseRepository.save(course);
            return "Course created successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to create course";
        }
    }
    /*
    @Override
    public String createCourse(Course course) {
       // azureBlobStorage.createContainer();
        Course course1 = new Course.Builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .imagepath("/images/java.png")
                .creationdate(new Date())
                .modificationdate(new Date())
                .reasonforrefusal(null)
                .build();
        return "";
    }

 */

}
