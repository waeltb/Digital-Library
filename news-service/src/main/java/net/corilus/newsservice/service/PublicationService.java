package net.corilus.newsservice.service;

import com.azure.storage.blob.BlobContainerClient;
import net.corilus.newsservice.dto.PublicationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PublicationService {
     String createPublication( PublicationDto publicationDto, MultipartFile image) throws IOException;

     ResponseEntity<PublicationDto> getPublicationById(Long id);


     List<PublicationDto> getAllPublications() ;

     ResponseEntity<PublicationDto> updatePublication( Long id,  PublicationDto publicationDto) ;


     ResponseEntity<Void> deletePublication( Long id) ;
    BlobContainerClient getBlobContainerClient(String containerName);
}
