package net.corilus.userservice.controller;


import net.corilus.userservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/profile")
public class ProfileController {
@Autowired
    private ProfileService profileService;
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("username") String username) throws IOException {
            profileService.uploadImage(file,username);
            return "success";

    }
    @GetMapping("/getimage")
    public ResponseEntity<Resource> getImage(@RequestParam("username") String username) {
        return profileService.getImage(username);
    }
}
