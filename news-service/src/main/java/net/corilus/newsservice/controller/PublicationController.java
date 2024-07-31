package net.corilus.newsservice.controller;

import net.corilus.newsservice.dto.PublicationDto;

import net.corilus.newsservice.service.PublicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/publication")
public class PublicationController {
    @Autowired
    PublicationServiceImpl publicationService;
    @PostMapping("/addPublication")
public String addPublication(@ModelAttribute PublicationDto publicationDto,
                             @RequestParam("imageFile") MultipartFile image) throws IOException {
        System.out.println("publicationDto = " + publicationDto);
        return publicationService.createPublication(publicationDto,image);

    }
    @PostMapping("/test")
    public String test() {
        return "test";

    }
}
