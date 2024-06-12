package net.corilus.courseservice.controller;

import net.corilus.courseservice.dto.Container;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.repository.CourseRepository;
import net.corilus.courseservice.service.AzureBlobStorageImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/test")
public class Test {
    @Autowired
    CourseRepository courseRepository ;
    @Autowired
    AzureBlobStorageImpl azureBlobStorage ;
    @GetMapping("/test1")
    public String test() {
            return "test";
    }
    @PostMapping("/addcourse")
    public String test1( @RequestBody Course course) {
        Course course1 = new Course.Builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .imagepath("/images/java.png")
                .creationdate(new Date())
                .modificationdate(new Date())
                .reasonforrefusal(null)
                .build();
        courseRepository.save(course1);
    return "succes";
    }
    @PostMapping("/addContainer")
    public String addContainer(@RequestBody Container container){

        azureBlobStorage.createContainer(container);
        return "ajout avec succes";
    }
}
