package net.corilus.courseservice.service;

import com.azure.storage.blob.BlobContainerClient;
import net.corilus.courseservice.dto.CourseDto;

import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {


    String updateCourse(CourseDto courseDto);
    String deleteCourse(int id);
    List<Speciality> getSpeciality();
    List<User> getAvailableExperts(String specialityName);
    String uploadFileToAzure(MultipartFile file, String directory, String containerName)throws IOException;
    BlobContainerClient getBlobContainerClient(String containerName);

}
