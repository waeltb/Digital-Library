package net.corilus.courseservice.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import net.corilus.courseservice.dto.Container;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AzureBlobStorageImpl implements IAzureBlobStorage {
    @Value("${azure.storage.connection.string}")
    private String connectionString;

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
