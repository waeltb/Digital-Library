package net.corilus.courseservice.controller;

import net.corilus.courseservice.dto.LessonDto;
import net.corilus.courseservice.entity.Lesson;
import net.corilus.courseservice.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @PostMapping("/addLessons")
    public ResponseEntity<String> addLessons(@ModelAttribute Lesson lesson,
                                @RequestParam("lessonVideo") MultipartFile videoLesson
                                             ) throws IOException {
        System.out.println("Received lessonDto: " + lesson);
        lessonService.createLesson(lesson,videoLesson);
        return ResponseEntity.ok("Lessons added successfully to the latest course");
    }
    @DeleteMapping("/deleteLessonById")
    public void DeleteLesson(@RequestParam("lessonId") Integer lessonId){
        lessonService.DeleteLesson(lessonId);
    }

}
