package net.corilus.newsservice.service;

import com.azure.core.exception.ResourceNotFoundException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import net.corilus.newsservice.dto.ImmutablePublicationDto;
import net.corilus.newsservice.dto.PublicationDto;
import net.corilus.newsservice.entity.Publication;
import net.corilus.newsservice.feignclient.UserClient;
import net.corilus.newsservice.modal.User;
import net.corilus.newsservice.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    PublicationRepository publicationRepository;
    @Autowired
   UserClient userClient;

    @Value("${azure.storage.connection.string}")
    private String azureStorageConnectionString;

    @Override
    public String createPublication(PublicationDto publicationDto, MultipartFile image) throws IOException {
        String containerName = publicationDto.getContainername();
        String imagePath = uploadFileToAzure(image, "publication", containerName);
        Publication publication = convertToEntity(publicationDto, imagePath);
        publicationRepository.save(publication);
        return "success";
    }

    @Override
    public ResponseEntity<PublicationDto> getPublicationById(Long id) throws UnsupportedEncodingException {
        Publication publication = publicationRepository.findById(id).get();
        PublicationDto publicationDto = convertToDto(publication);
        return ResponseEntity.ok(publicationDto);
    }
    @Override
    public ResponseEntity<byte[]> getImageById(Long id) throws IOException {
        Publication publication = publicationRepository.findById(id).get();
        // Obtenez le chemin relatif de l'image (par exemple "publication/4a9a57e2-2e74-4bcf-96c3-ba06102542af.jpeg")
        String imageUrl = publication.getImage();
        System.out.println(imageUrl);
        String containerName = publication.getContainername();

        byte[] imageBytes = getImageFromAzure(imageUrl, containerName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @Override
    public List<PublicationDto> getAllPublications() {
        List<Publication> publications = publicationRepository.findAll();
        return publications.stream()
                .map(publication -> {
                    try {
                        return convertToDto(publication);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("Error converting publication to DTO", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updatePublication(Long id, PublicationDto publicationDto, MultipartFile image) throws IOException {
        //delete image from azure
        Publication publication = publicationRepository.findById(id).get();
        PublicationDto DtoPublication = convertToDto(publication);
        String imagePath = DtoPublication.getImage();
        String containerName = DtoPublication.getContainername();
        String blobName = extractFromImagePath(imagePath,"publication");
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();

        // upload new image
        String newImagePath = uploadFileToAzure(image, "publication", containerName);



        publication.setSpeciality(publicationDto.getSpeciality());
        publication.setDescription(publicationDto.getDescription());
        publication.setTitle(publicationDto.getTitle());
        publication.setCreationdate(new Date());
        publication.setImage(newImagePath);
        publicationRepository.save(publication);

    }

    @Override
    public ResponseEntity<Void> deletePublication(Long id) throws UnsupportedEncodingException {
        Publication publication = publicationRepository.findById(id).get();
        PublicationDto publicationDto = convertToDto(publication);
        String imagePath = publicationDto.getImage();
        String containerName = publicationDto.getContainername();
        String blobName = extractFromImagePath(imagePath,"publication");
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();


        publicationRepository.delete(publication);

        // Retourner une réponse 204 No Content
        return ResponseEntity.noContent().build();
    }


    @Override
    public BlobContainerClient getBlobContainerClient(String containerName) {
        return new BlobContainerClientBuilder()
                .connectionString(azureStorageConnectionString)
                .containerName(containerName)
                .buildClient();
    }
    @Override
    public Publication convertToEntity(PublicationDto publicationDto, String imagePath) {
        return Publication.builder()
                .title(publicationDto.getTitle())
                .description(publicationDto.getDescription())
                .creationdate(new Date())
                .image(imagePath)
                .speciality(publicationDto.getSpeciality())
                .containername(publicationDto.getContainername())
                .build();
    }
    @Override
    public String uploadFileToAzure(MultipartFile file, String directory, String containerName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        String uniqueFileName = directory + "/" + file.getOriginalFilename();
        System.out.println("uniqueFileName = " + uniqueFileName);
        BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);

    return uniqueFileName;
    }
    @Override

    public String getImageUrl(String imagePath, String containerName) throws UnsupportedEncodingException {
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(imagePath);
        String encodedUrl = blobClient.getBlobUrl();
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.name());
    }
    @Override
    public PublicationDto convertToDto(Publication publication) throws UnsupportedEncodingException {
        String imageUrl = getImageUrl(publication.getImage(), publication.getContainername());
        User user = userClient.getUserById((long) publication.getAuthorId());
        return ImmutablePublicationDto.builder()
                .title(publication.getTitle())
                .description(publication.getDescription())
                .image(imageUrl)
                .speciality(publication.getSpeciality())
                .containername(publication.getContainername())
                .author(publication.getAuthorId())
                .username(user.getUsername())
                .id(publication.getId())
                .creationdate(publication.getCreationdate())
                .build();
    }
    @Override
    public byte[] getImageFromAzure(String imagePath, String containerName) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(imagePath);

       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.download(outputStream);
            return outputStream.toByteArray();

    }
    @Override
    public  String extractFromImagePath(String imagePath, String keyword) {
        // Trouver la position du mot-clé dans le chemin de l'image
        int startIndex = imagePath.indexOf(keyword);

        // Si le mot-clé est trouvé, retourner le reste de la chaîne à partir de ce mot-clé
        if (startIndex != -1) {
            return imagePath.substring(startIndex);
        } else {
            // Si le mot-clé n'est pas trouvé, retourner une chaîne vide ou un message d'erreur
            return "Keyword not found in the image path.";
        }
    }

    @Override
    public User getUserById(Long idUser) {
        return userClient.getUserById(idUser);
    }


}
