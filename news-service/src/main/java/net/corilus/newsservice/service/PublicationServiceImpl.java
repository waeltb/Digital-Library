package net.corilus.newsservice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import net.corilus.newsservice.dto.PublicationDto;
import net.corilus.newsservice.entity.Publication;
import net.corilus.newsservice.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    PublicationRepository publicationRepository;



    @Value("${azure.storage.connection.string}")
    private String azureStorageConnectionString;

    @Override
    public String createPublication(PublicationDto publicationDto, MultipartFile image) throws IOException {
        String containerName = publicationDto.getContainername();
        String imagePath = uploadFileToAzure(image, "publication",containerName);

        Publication publication = convertToEntity(publicationDto, imagePath);
       publicationRepository.save(publication);
        return "success";
    }

    @Override
    public ResponseEntity<PublicationDto> getPublicationById(Long id) {
        return null;
    }

    @Override
    public List<PublicationDto> getAllPublications() {
        return List.of();
    }

    @Override
    public ResponseEntity<PublicationDto> updatePublication(Long id, PublicationDto publicationDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletePublication(Long id) {
        return null;
    }

    @Override
    public BlobContainerClient getBlobContainerClient(String containerName) {
        return new BlobContainerClientBuilder()
                .connectionString(azureStorageConnectionString)
                .containerName(containerName)
                .buildClient();
    }

    private Publication convertToEntity(PublicationDto publicationDto, String imagePath) {
        return Publication.builder()
                .title(publicationDto.getTitle())
                .description(publicationDto.getDescription())
                .creationdate(new Date())
                .image(imagePath)
                .speciality(publicationDto.getSpeciality())
                .containername(publicationDto.getContainername())
                .build();
    }

    private String uploadFileToAzure(MultipartFile file, String directory, String containerName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        String uniqueFileName = directory + "/" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }
}
