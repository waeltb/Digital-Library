package net.corilus.userservice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    @Value("${azure.storage.container.useraccount}")
    private String containerUserAccount;


    @Value("${azure.storage.connection.string}")
    private String connectionString;
    @Autowired
    private BlobServiceClient blobServiceClient ;
    @Override
    public void uploadImage(MultipartFile file,String username) throws IOException {
        String fileName = username + ".png";

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("useraccount");
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        blobClient.upload(file.getInputStream(), file.getSize(), true);
    }

    @Override
    public ResponseEntity<Resource> getImage(String username) {
        String fileName = username + ".png";
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerUserAccount);
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Download the blob to an InputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        InputStreamResource resource = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png"); // or the appropriate mime type
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
