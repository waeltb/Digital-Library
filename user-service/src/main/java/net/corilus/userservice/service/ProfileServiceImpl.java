package net.corilus.userservice.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
}
