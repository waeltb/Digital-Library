package net.corilus.courseservice.controller;

import net.corilus.courseservice.dto.CourseDto;
import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;
import net.corilus.courseservice.service.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseServiceImpl courseService;

    @PostMapping("/addCourse/{nameSpeciality}")
    public String addCourse(@ModelAttribute CourseDto courseDto,
                            @RequestParam("videoFile") MultipartFile videoFile,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            @PathVariable String nameSpeciality) {
        try {
            courseService.createCourse(courseDto, videoFile, imageFile, nameSpeciality);
            return "Course added successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to add course";
        }
    }

    @GetMapping("/getSpeciality")
    public ResponseEntity<?> getSpeciality() {
        List<Speciality> specialities = courseService.getSpeciality();
        return ResponseEntity.ok(specialities);
    }
    @GetMapping("/getAvailableExperts/{specialityName}")
    public ResponseEntity<?> getAvailableExperts(@PathVariable String specialityName) {
        List<User> users = courseService.getAvailableExperts(specialityName);
        return ResponseEntity.ok(users);
    }
}
