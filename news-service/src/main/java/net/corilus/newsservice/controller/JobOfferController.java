package net.corilus.newsservice.controller;

import net.corilus.newsservice.dto.JobOfferDto;

import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import net.corilus.newsservice.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobOffer")
public class JobOfferController {
    @Autowired
     JobOfferService jobOfferService;
    @PostMapping("/addJobOffer")
    public void addJobOffer(@ModelAttribute JobOfferDto jobOfferDto,
                                 @RequestParam("imageFile") MultipartFile image) throws IOException {

         jobOfferService.createJobOffer(jobOfferDto,image);

    }
    @GetMapping("/getJobOfferById/{idJobOffer}")
    public JobOfferDto getJobOfferById(@PathVariable("idJobOffer")Long id){
        return jobOfferService.getJobOfferById(id);
    }
    @GetMapping("/getImageJobOfferById")
    public ResponseEntity<byte[]> getImageJobOfferById(@RequestParam("idJobOffer")Long id) throws IOException {
        return jobOfferService.getImageById(id);
    }
    @DeleteMapping("/deleteJobOffer/{idJobOffer}")
    public void deleteJobOffer(@PathVariable("idJobOffer") Long id) throws UnsupportedEncodingException {
         jobOfferService.deleteJobOffer(id);
    }
    @GetMapping("/job-offersAll")
    public Page<JobOfferDto> getJobOffersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersPaginated( page, size);
    }
    @GetMapping("/job-offersByCategory")
    public Page<JobOfferDto> getJobOffersByCategory(
            @RequestParam Categories category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersByCategoryPaginated(category, page, size);
    }
    @GetMapping("/job-offersByLocation")
    public Page<JobOfferDto> getJobOffersByLocationPaginated(
            @RequestParam String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersByLocationPaginated(location, page, size);
    }
    @GetMapping("/job-offersByExperienceLevelPaginated")
    public Page<JobOfferDto> getJobOffersByExperienceLevelPaginated(
            @RequestParam ExperienceLevel experienceLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersByExperienceLevelPaginated(experienceLevel, page, size);
    }
    @GetMapping("/job-offersByExperienceCompanyPaginated")
    public Page<JobOfferDto> getJobOffersByCompanyPaginated(
            @RequestParam String company,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersByCompanyPaginated(company, page, size);
    }
    @GetMapping("/job-offersBySalaryRangePaginated")
    public Page<JobOfferDto> getJobOffersBySalaryRangePaginated(
            @RequestParam String salary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobOfferService.getJobOffersBySalaryRangePaginated(salary, page, size);
    }
    @GetMapping("/getCategories")
    public ResponseEntity<?>  getCategories(){
        return ResponseEntity.ok(jobOfferService.getCategories());
    }
    @GetMapping("/getExperienceLevels")
    public ResponseEntity<?> getExperienceLevels(){
        return ResponseEntity.ok(jobOfferService.getExperienceLevels());
    }


}
