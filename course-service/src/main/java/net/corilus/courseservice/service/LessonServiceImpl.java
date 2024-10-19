package net.corilus.courseservice.service;

import net.corilus.courseservice.dto.LessonDto;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.entity.Lesson;
import net.corilus.courseservice.entity.Slide;
import net.corilus.courseservice.repository.CourseRepository;
import net.corilus.courseservice.repository.LessonRepository;
import net.corilus.courseservice.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SlideRepository slideRepository;
    @Autowired
    private CourseService courseService;
    @Value("${azure.storage.connection.string}")
    private String azureStorageConnectionString;

    @Override
    public void createLesson(Lesson lesson, MultipartFile videoLesson) throws IOException {
        String containerName = lesson.getContainername();
        String videoPath = courseService.uploadFileToAzure(videoLesson, "video",containerName);


        // First, save the lesson to generate an ID
        Lesson savedLesson = lessonRepository.save(lesson);

        // Process slides after the lesson has been saved and has a valid ID
        List<Slide> slidesToSave = lesson.getSlides().stream().map(slide ->
                Slide.builder()
                        .title(slide.getTitle())
                        .order(slide.getOrder())  // Ensure that `order` is an int
                        .pathvideo(slide.getPathvideo())
                        .lesson(savedLesson)  // Set the saved lesson for the slide
                        .build()
        ).collect(Collectors.toList());

        // Save the slides with the correct lesson ID
        slideRepository.saveAll(slidesToSave);


    }


    @Override
    public void DeleteLesson(Integer lessonId) {
Lesson lesson = lessonRepository.findById(Long.valueOf(lessonId)).get();
lessonRepository.delete(lesson);
    }



    @Override
    public Lesson convertToEntity(LessonDto lessonDto) {
        // Conversion d'un DTO en entité Lesson
        return Lesson.builder()
                .title(lessonDto.getTitle())
                .build();
    }
}
