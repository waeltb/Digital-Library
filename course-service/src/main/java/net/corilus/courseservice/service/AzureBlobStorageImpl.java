package net.corilus.courseservice.service;

import com.azure.storage.blob.*;
import lombok.extern.slf4j.Slf4j;
import net.corilus.courseservice.dto.Container;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class AzureBlobStorageImpl implements IAzureBlobStorage {
    @Value("${azure.storage.connection.string}")
    private String connectionString;
    @Bean
    public void createContainerUser() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        System.out.println("blobservice est " + blobServiceClient);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("user1");
        System.out.println("containerclient est " + containerClient);
        containerClient.create();
    }
    private BlobContainerClient getBlobContainerClient(String containername) {
        return new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containername)
                .buildClient();
    }
    public String uploadFile(MultipartFile file) throws IOException {
        BlobContainerClient containerClient = getBlobContainerClient("user1");
        // Chemin de fichier unique avec dossier images
        String uniqueFileName ="/images/" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }
    @Override
    public void createContainer(Container container) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        System.out.println("blobservice est " + blobServiceClient);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container.getNameContainer());
        System.out.println("containerclient est " + containerClient);
        containerClient.create();
    }


    @Override
    public void deleteContainer(String nameContainer) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        blobServiceClient.deleteBlobContainer(nameContainer);

    }
}
