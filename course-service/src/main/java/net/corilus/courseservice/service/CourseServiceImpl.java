package net.corilus.courseservice.service;

import com.azure.storage.blob.BlobClient;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import net.corilus.courseservice.dto.CourseDto;
import net.corilus.courseservice.entity.Course;
import net.corilus.courseservice.entity.Status;
import net.corilus.courseservice.feignclient.UserClient;
import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;
import net.corilus.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserClient userClient ;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Value("${azure.storage.connection.string}")
    private String azureStorageConnectionString;

    @Override
    public String createCourse(CourseDto courseDto, MultipartFile videoFile, MultipartFile imageFile, String nameSpeciality) throws IOException {
        String videoPath = uploadFileToAzure(videoFile, "videos");
        String imagePath = uploadFileToAzure(imageFile, "images");

        Course course = convertToEntity(courseDto, nameSpeciality, videoPath, imagePath);
        courseRepository.save(course);
        return "success";
    }

    @Override
    public String updateCourse(CourseDto courseDto) {
        return "";
    }

    @Override
    public String deleteCourse(int id) {
        return "";
    }

    @Override
    public List<Speciality> getSpeciality() {
        return userClient.getSpeciality();
    }

    @Override
    public List<User> getAvailableExperts(String specialityName) {
        return userClient.getAvailableExperts(specialityName);
    }

    private Course convertToEntity(CourseDto courseDto, String nameSpeciality, String videoPath, String imagePath) {
        return Course.builder()
                .title(courseDto.getTitle())
                .price(courseDto.getPrice())
                .description(courseDto.getDescription())
                .creationdate(new Date())
                .descriptiveVideo(videoPath)
                .image(imagePath)
                .speciality(nameSpeciality)
                .level(courseDto.getLevel())
                .status(Status.INPROGRESS)
                .build();
    }

    private String uploadFileToAzure(MultipartFile file, String directory) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient(containerName);
        // Chemin de fichier unique avec dossier approprié
        String uniqueFileName = directory + "/" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }

    private BlobContainerClient getBlobContainerClient(String containerName) {
        return new BlobContainerClientBuilder()
                .connectionString(azureStorageConnectionString)
                .containerName(containerName)
                .buildClient();
    }
}
