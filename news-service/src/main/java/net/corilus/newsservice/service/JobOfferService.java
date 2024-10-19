package net.corilus.newsservice.service;

import net.corilus.newsservice.dto.JobOfferDto;


import net.corilus.newsservice.entity.JobOffer;

import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.data.domain.Page;

public interface JobOfferService {
    void createJobOffer(JobOfferDto jobOfferDto, MultipartFile image) throws IOException;
    JobOffer convertToEntity(JobOfferDto jobOfferDto, String imagePath);
    void deleteJobOffer(Long idJobOffer) throws UnsupportedEncodingException;
    JobOfferDto convertToDto(JobOffer jobOffer) throws UnsupportedEncodingException;
    Page<JobOfferDto>getJobOffersByCategoryPaginated(Categories categorie,int page, int size);
    Page<JobOfferDto>getJobOffersByLocationPaginated(String location,int page, int size);
    Page<JobOfferDto>getJobOffersByExperienceLevelPaginated(ExperienceLevel experienceLevel, int page, int size);
    Page<JobOfferDto>getJobOffersByCompanyPaginated(String company, int page, int size);
    Page<JobOfferDto>getJobOffersBySalaryRangePaginated(String salaryRange, int page, int size);
    ResponseEntity<byte[]> getImageById(Long id) throws IOException;
    Page<JobOfferDto>getJobOffersPaginated(int page, int size);
    List<Categories> getCategories();
    List<ExperienceLevel> getExperienceLevels();
    JobOfferDto getJobOfferById(Long id) ;
}
