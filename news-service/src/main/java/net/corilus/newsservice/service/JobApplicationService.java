package net.corilus.newsservice.service;

import net.corilus.newsservice.dto.JobApplicationDto;
import net.corilus.newsservice.entity.JobApplication;
import net.corilus.newsservice.entity.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface JobApplicationService {
    void createJobApplication(JobApplicationDto jobApplicationDto, MultipartFile image) throws IOException;
    JobApplication convertToEntity(JobApplicationDto jobApplicationDto, String imagePath,JobOffer joboffer);
    Page<JobApplicationDto> getJobOfferApplicationByOfferIdPaginated(int offerId, int page, int size);
    JobApplicationDto convertToDto(JobApplication jobApplication) throws UnsupportedEncodingException;
    Page<JobApplicationDto> getJobOfferApplicationByUserIdPaginated(int userId, int page, int size);
    Page<JobApplicationDto> getJobOfferApplicationByUserNamePaginated(String username, int page, int size);
     Page<JobApplicationDto> getAcceptedApplicationsByUserId(String username,  int page, int size);
    Page<JobApplicationDto> getInProgressApplicationsByUserId(String username,  int page, int size);
    Page<JobApplicationDto> getRefusedApplicationsByUserId(String username,  int page, int size);

}
