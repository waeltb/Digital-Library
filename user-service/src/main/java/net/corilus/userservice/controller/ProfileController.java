package net.corilus.userservice.controller;


import net.corilus.userservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
