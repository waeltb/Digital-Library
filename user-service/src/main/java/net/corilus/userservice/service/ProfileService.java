package net.corilus.userservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
void uploadImage(MultipartFile file,String username) throws IOException;
}
