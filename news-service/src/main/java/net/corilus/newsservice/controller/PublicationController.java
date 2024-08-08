package net.corilus.newsservice.controller;

import net.corilus.newsservice.dto.PublicationDto;

import net.corilus.newsservice.modal.User;
import net.corilus.newsservice.service.PublicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
  @GetMapping("/getPublicationById/{idPublication}")
    ResponseEntity<PublicationDto> getPublicationById(@PathVariable("idPublication") Long id) throws UnsupportedEncodingException {
        return publicationService.getPublicationById(id);

    }
    @GetMapping("/getImagePublicationById")
    public ResponseEntity<byte[]> getImagePublicationById(@RequestParam("idPublication")Long id) throws IOException {
        return publicationService.getImageById(id);
    }
    @GetMapping("/getAllPublications")
    public List<PublicationDto> getAllPublications() {
        return publicationService.getAllPublications();
    }
    @DeleteMapping("/deletePublication/{idPublication}")
    public ResponseEntity<Void> deletePublication( @PathVariable("idPublication") Long id) throws UnsupportedEncodingException {
        return publicationService.deletePublication(id);
    }
    @PutMapping("/updatePublication")
    public void updatePublication(@RequestParam("idPublication") Long id, @ModelAttribute PublicationDto publicationDto, @RequestParam("imageFile") MultipartFile image) throws IOException {
        publicationService.updatePublication(id, publicationDto,image);
    }
    @GetMapping("/getUserById/{idUser}")
    public ResponseEntity<?> getUserById(@PathVariable Long idUser) {
        User user = publicationService.getUserById(idUser);
        return ResponseEntity.ok(user);
    }





    }
