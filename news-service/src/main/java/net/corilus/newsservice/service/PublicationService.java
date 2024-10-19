package net.corilus.newsservice.service;

import com.azure.storage.blob.BlobContainerClient;
import net.corilus.newsservice.dto.PublicationDto;
import net.corilus.newsservice.entity.Publication;
import net.corilus.newsservice.modal.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PublicationService {
     String createPublication( PublicationDto publicationDto, MultipartFile image) throws IOException;
     ResponseEntity<PublicationDto> getPublicationById(Long id) throws UnsupportedEncodingException;
      ResponseEntity<byte[]> getImageById(Long id) throws IOException;
      byte[] getImageFromAzure(String imagePath, String containerName) throws IOException;
     List<PublicationDto> getAllPublications() ;
     Publication convertToEntity(PublicationDto publicationDto, String imagePath);
     void updatePublication( Long id,  PublicationDto publicationDto,MultipartFile image) throws IOException;
     PublicationDto convertToDto(Publication publication) throws UnsupportedEncodingException;
     String getImageUrl(String imagePath, String containerName) throws UnsupportedEncodingException;
     ResponseEntity<Void> deletePublication( Long id) throws UnsupportedEncodingException;
    BlobContainerClient getBlobContainerClient(String containerName);
     String uploadFileToAzure(MultipartFile file, String directory, String containerName)throws IOException;
      String extractFromImagePath(String imagePath, String keyword) ;
    User getUserById(Long idUser);
}
