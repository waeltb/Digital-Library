package net.corilus.newsservice.service;

import net.corilus.newsservice.dto.ImmutableJobApplicationDto;
import net.corilus.newsservice.dto.JobApplicationDto;
import net.corilus.newsservice.entity.JobApplication;
import net.corilus.newsservice.entity.JobOffer;
import net.corilus.newsservice.enums.ApplicationStatus;
import net.corilus.newsservice.feignclient.UserClient;
import net.corilus.newsservice.modal.User;
import net.corilus.newsservice.repository.JobApplicationRepository;
import net.corilus.newsservice.repository.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private PublicationServiceImpl publicationService;
    @Autowired
    UserClient userClient;

    @Override
    public void createJobApplication(JobApplicationDto jobApplicationDto, MultipartFile image) throws IOException {
        Optional<JobOffer> optionalJobOffer = jobOfferRepository.findById(jobApplicationDto.getJobOfferId());
        if (optionalJobOffer.isPresent()) {
            JobOffer jobOffer = optionalJobOffer.get();
            String containerName = jobApplicationDto.getContainername();
            String imagePath = publicationService.uploadFileToAzure(image, "job", containerName);
            JobApplication jobApplication = convertToEntity(jobApplicationDto, imagePath, jobOffer);
            jobApplicationRepository.save(jobApplication);
        } else {
            throw new IllegalArgumentException("Job offer not found for ID: " + jobApplicationDto.getJobOfferId());
        }
    }

    @Override
    public JobApplication convertToEntity(JobApplicationDto jobApplicationDto, String imagePath, JobOffer jobOffer) {
        return JobApplication.builder()
                .jobOffer(jobOffer)
                .containername(jobApplicationDto.getContainername())
                .applicationDate(new Date())
                .coverLetter(jobApplicationDto.getCoverLetter())
                .cv(imagePath)
                .firstName(jobApplicationDto.getFirstName())
                .lastName(jobApplicationDto.getLastName())
                .status(ApplicationStatus.IN_PROGRESS)
                .email(jobApplicationDto.getEmail())
                .build();
    }

    @Override
    public Page<JobApplicationDto> getJobOfferApplicationByOfferIdPaginated(int offerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByJobOffer_OfferId(offerId, pageable);
        return jobApplicationPage.map(this::convertToDto);
    }

    @Override
    public JobApplicationDto convertToDto(JobApplication jobApplication) {

        try {
            JobOffer jobOffer = jobApplication.getJobOffer();
            String imageUrl = publicationService.getImageUrl(jobApplication.getCv(), jobApplication.getContainername());
            return ImmutableJobApplicationDto.builder()
                    .firstName(jobApplication.getFirstName())
                    .lastName(jobApplication.getLastName())
                    .coverLetter(jobApplication.getCoverLetter())
                    .cv(imageUrl)
                    .applicationDate(jobApplication.getApplicationDate())
                    .applicationStatus(jobApplication.getStatus())
                    .jobOfferId((long) jobApplication.getJobOffer().getOfferId())
                    .applicationId(jobApplication.getApplicationId())
                    .userId(jobApplication.getUserId())
                    .containername(jobApplication.getContainername())
                    .email(jobApplication.getEmail())
                    .companyName(jobOffer.getCompany())
                    .titleOffer(jobOffer.getTitle())


                    .build();
        } catch (UnsupportedEncodingException e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw new RuntimeException("Error converting job application to DTO", e);
        }
    }

    @Override
    public Page<JobApplicationDto> getJobOfferApplicationByUserIdPaginated(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByUserId(userId, pageable);
        return jobApplicationPage.map(this::convertToDto);


    }

    @Override
    public Page<JobApplicationDto> getJobOfferApplicationByUserNamePaginated(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userClient.getUser(username);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByUserId(user.getId(), pageable);
        return jobApplicationPage.map(this::convertToDto);
    }

    @Override
    public Page<JobApplicationDto> getAcceptedApplicationsByUserId(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userClient.getUser(username);
        Page<JobApplication> jobApplicationPage =jobApplicationRepository.findByUserIdAndStatus(user.getId(), ApplicationStatus.ACCEPTED, pageable);
        return jobApplicationPage.map(this::convertToDto);

    }

    @Override
    public Page<JobApplicationDto> getInProgressApplicationsByUserId(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userClient.getUser(username);
        Page<JobApplication> jobApplicationPage =jobApplicationRepository.findByUserIdAndStatus(user.getId(), ApplicationStatus.IN_PROGRESS, pageable);
        return jobApplicationPage.map(this::convertToDto);
    }

    @Override
    public Page<JobApplicationDto> getRefusedApplicationsByUserId(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userClient.getUser(username);
        Page<JobApplication> jobApplicationPage =jobApplicationRepository.findByUserIdAndStatus(user.getId(), ApplicationStatus.REFUSED, pageable);
        return jobApplicationPage.map(this::convertToDto);
    }
}
