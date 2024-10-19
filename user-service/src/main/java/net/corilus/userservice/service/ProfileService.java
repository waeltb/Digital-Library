package net.corilus.userservice.service;


public interface ProfileService {


import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
void uploadImage(MultipartFile file,String username) throws IOException;
ResponseEntity<Resource> getImage(String username);

}
