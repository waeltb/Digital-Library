package net.corilus.courseservice.service;

import net.corilus.courseservice.dto.CourseDto;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    String createCourse(CourseDto courseDto, MultipartFile videoFile, MultipartFile imageFile, String nameSpeciality) throws IOException;
    String updateCourse(CourseDto courseDto);
    String deleteCourse(int id);
    List<Speciality> getSpeciality();
    List<User> getAvailableExperts(String specialityName);
}
