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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserClient userClient ;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Value("${azure.storage.connection.string}")
    private String azureStorageConnectionString;


    public ResponseEntity<String> addCourse(CourseDto courseDto) throws IOException {



        List<User> availableExperts = userClient.getAvailableExperts(courseDto.getSpeciality());

        String videoPath = uploadFileToAzure(courseDto.getVideoFile(), "video", containerName);
        if (videoPath == null || videoPath.isEmpty()) {
            throw new IOException("Failed to upload video file to Azure.");
        }

        String imagePath = uploadFileToAzure(courseDto.getImageFile(), "picture", containerName);
        if (imagePath == null || imagePath.isEmpty()) {
            throw new IOException("Failed to upload image file to Azure.");
        }

        Course course;
        if (availableExperts.isEmpty()) {
            course = convertToEntityCourse(courseDto, videoPath, imagePath, null, Status.PENDING);
        } else {
            int expertId = availableExperts.get(0).getId();
            course = convertToEntityCourse(courseDto, videoPath, imagePath, expertId, Status.INPROGRESS);
        }

        courseRepository.save(course);

        return new ResponseEntity<>("Course added successfully", HttpStatus.OK);
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



    private Course convertToEntityCourse(CourseDto courseDto, String videoPath, String imagePath, Integer expertId, Status status) {
        return Course.builder()
                .title(courseDto.getTitle())
                .price(courseDto.getPrice())
                .description(courseDto.getDescription())
                .creationdate(new Date())
                .descriptiveVideo(videoPath)
                .image(imagePath)
                .speciality(courseDto.getSpeciality())
                .level(courseDto.getLevel())
                .status(status)
                .expertId(expertId)
                .language(courseDto.getLanguage())
                .owner(courseDto.getOwner())
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
    public BlobContainerClient getBlobContainerClient(String containerName) {
        return new BlobContainerClientBuilder()
                .connectionString(azureStorageConnectionString)
                .containerName(containerName)
                .buildClient();
    }


}
