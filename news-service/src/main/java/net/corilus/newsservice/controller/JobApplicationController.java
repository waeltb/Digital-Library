package net.corilus.newsservice.controller;

import net.corilus.newsservice.dto.JobApplicationDto;

import net.corilus.newsservice.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobApplication")
public class JobApplicationController {
    @Autowired
    JobApplicationService jobApplicationService;
    @PostMapping("/addJobApplication")
    public void addJobApplication(@ModelAttribute JobApplicationDto jobApplicationDto,
                            @RequestParam("imageFile") MultipartFile image) throws IOException {

        jobApplicationService.createJobApplication(jobApplicationDto,image);

    }
    @GetMapping("/getJobOfferById")
    public Page<JobApplicationDto> getJobApplicationByOfferId(@RequestParam int offerId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getJobOfferApplicationByOfferIdPaginated(offerId,page,size);
    }
    @GetMapping("/getJobOfferByUserId")
    public Page<JobApplicationDto> getJobOfferApplicationByUserId(@RequestParam int userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getJobOfferApplicationByUserIdPaginated(userId,page,size);
    }
    @GetMapping("/getJobOfferByUserName")
    public Page<JobApplicationDto> getJobOfferApplicationByUserName(@RequestParam String username,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getJobOfferApplicationByUserNamePaginated(username,page,size);
    }
    @GetMapping("/getAcceptedApplications")
    public Page<JobApplicationDto> getAcceptedApplications(@RequestParam String username,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getAcceptedApplicationsByUserId(username,page,size);
    }
    @GetMapping("/getInProgressApplicationsByUserId")
    public Page<JobApplicationDto> getInProgressApplicationsByUserId(@RequestParam String username,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getInProgressApplicationsByUserId(username,page,size);
    }
    @GetMapping("/getRefusedApplicationsByUserId")
    public Page<JobApplicationDto> getRefusedApplicationsByUserId(@RequestParam String username,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size){
        return  jobApplicationService.getRefusedApplicationsByUserId(username,page,size);
    }


}
