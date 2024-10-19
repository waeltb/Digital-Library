package net.corilus.newsservice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import lombok.SneakyThrows;
import net.corilus.newsservice.dto.ImmutableJobOfferDto;
import net.corilus.newsservice.dto.JobOfferDto;
import net.corilus.newsservice.entity.JobOffer;
import net.corilus.newsservice.entity.Publication;
import net.corilus.newsservice.enums.Categories;
import net.corilus.newsservice.enums.ExperienceLevel;
import net.corilus.newsservice.repository.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {
    @Autowired
    JobOfferRepository jobOfferRepository;
    @Autowired
    PublicationServiceImpl publicationService;
    @Override
    public void createJobOffer(JobOfferDto jobOfferDto, MultipartFile image) throws IOException {
        String containerName = jobOfferDto.getContainername();
        String imagePath = publicationService.uploadFileToAzure(image, "job", containerName);
        JobOffer jobOffer = convertToEntity(jobOfferDto, imagePath);
        jobOfferRepository.save(jobOffer);
    }

    @Override
    public JobOffer convertToEntity(JobOfferDto jobOfferDto, String imagePath) {
        return JobOffer.builder()
                .title(jobOfferDto.getTitle())
                .categories(jobOfferDto.getCategories())
                .company(jobOfferDto.getCompany())
                .containername(jobOfferDto.getContainername())
                .missions(jobOfferDto.getMissions())
                .skills(jobOfferDto.getSkills())
                .location(jobOfferDto.getLocation())
                .salaryRange(jobOfferDto.getSalaryRange())
                .logo(imagePath)
                .experienceLevel(jobOfferDto.getExperienceLevel())
                .endDateForSending(jobOfferDto.getEndDateForSending())
                .dateOfCreation(new Date())
                .build();
    }

    @Override
    public void deleteJobOffer(Long idJobOffer) throws UnsupportedEncodingException {
        JobOffer jobOffer = jobOfferRepository.findById(idJobOffer).orElse(null);
        JobOfferDto jobOfferDto=convertToDto(jobOffer);
        String imagePath = jobOfferDto.getLogo();
        String containerName = jobOfferDto.getContainername();
        String blobName = publicationService.extractFromImagePath(imagePath,"job");
        BlobContainerClient containerClient = publicationService.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();
        jobOfferRepository.delete(jobOffer);
    }

    @Override
    public JobOfferDto convertToDto(JobOffer jobOffer) {
        try {
            String imageUrl = publicationService.getImageUrl(jobOffer.getLogo(), jobOffer.getContainername());
            return ImmutableJobOfferDto.builder()
                    .offerId(jobOffer.getOfferId())
                    .userId(jobOffer.getUserId())
                    .title(jobOffer.getTitle())
                    .missions(jobOffer.getMissions())
                    .skills(jobOffer.getSkills())
                    .location(jobOffer.getLocation())
                    .salaryRange(jobOffer.getSalaryRange())
                    .experienceLevel(jobOffer.getExperienceLevel())
                    .endDateForSending(jobOffer.getEndDateForSending())
                    .containername(jobOffer.getContainername())
                    .logo(imageUrl)
                    .categories(jobOffer.getCategories())
                    .company(jobOffer.getCompany())
                    .dateOfCreation(jobOffer.getDateOfCreation())
                    .build();
        } catch (UnsupportedEncodingException e) {
            // Gérer l'exception de manière appropriée, par exemple :
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la conversion de l'offre d'emploi en DTO", e);
        }
    }



    @Override
    public Page<JobOfferDto> getJobOffersByCategoryPaginated(Categories categorie, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findByCategories(categorie,pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public Page<JobOfferDto> getJobOffersByLocationPaginated(String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findByLocation(location,pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public Page<JobOfferDto> getJobOffersByExperienceLevelPaginated(ExperienceLevel experienceLevel, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findByExperienceLevel(experienceLevel,pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public Page<JobOfferDto> getJobOffersByCompanyPaginated(String company, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findByCompany(company,pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public Page<JobOfferDto> getJobOffersBySalaryRangePaginated(String salaryRange, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findBySalaryRangeAfter(salaryRange,pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public ResponseEntity<byte[]> getImageById(Long id) throws IOException {
        JobOffer jobOffer = jobOfferRepository.findById(id).orElse(null);
        // Obtenez le chemin relatif de l'image (par exemple "publication/4a9a57e2-2e74-4bcf-96c3-ba06102542af.jpeg")
        String imageUrl = jobOffer.getLogo();
        System.out.println(imageUrl);
        String containerName = jobOffer.getContainername();

        byte[] imageBytes = publicationService.getImageFromAzure(imageUrl, containerName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @Override
    public Page<JobOfferDto> getJobOffersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobOffer> pageJobOffer = jobOfferRepository.findAll(pageable);
        return pageJobOffer.map(this::convertToDto);
    }

    @Override
    public List<Categories> getCategories() {
        return List.of(Categories.values());
    }

    @Override
    public List<ExperienceLevel> getExperienceLevels() {
        return List.of(ExperienceLevel.values());
    }

    @Override
    public JobOfferDto getJobOfferById(Long id)  {
        JobOffer jobOffer = jobOfferRepository.findById(id).orElse(null);
        return convertToDto(jobOffer);
    }

    @Override
    public List<String> getAllCompanies() {
        return jobOfferRepository.findAllDistinctCompanies();
    }

    @Override
    public List<String> getAllDistinctLocations() {
        return jobOfferRepository.findAllDistinctLocations();
    }
}
