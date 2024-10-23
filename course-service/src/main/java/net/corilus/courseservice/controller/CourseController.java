package net.corilus.courseservice.controller;

import net.corilus.courseservice.dto.CourseDto;
import net.corilus.courseservice.entity.Level;
import net.corilus.courseservice.enums.Language;
import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;
import net.corilus.courseservice.service.CourseServiceImpl;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import java.io.IOException;


import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseServiceImpl courseService;

    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@ModelAttribute CourseDto courseDto) throws IOException {
         return courseService.addCourse(courseDto);

    }
    @GetMapping("/current-user-connected-course")
    public ResponseEntity<String> currentUserConnected(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok("response of testing current user auth "+jwt.getClaim(AccessToken.PREFERRED_USERNAME)+" And his id : "+jwt.getSubject());
    }
    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('admin')")
    public String getAdminData() {
        return "This is admin data.";
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('user')")
    public String getClientData() {
        return "This is client data.";
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
    @GetMapping("/getLevel")
    public ResponseEntity<Level[]> getAllLevels() {
        Level[] levels = Level.values();
        return ResponseEntity.ok(levels);
    }
    @GetMapping("/getAllLanguage")
    public ResponseEntity<Language[]> getAllLanguage() {
        Language[] languages = Language.values();
        return ResponseEntity.ok(languages);
    }
}
