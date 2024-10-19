package net.corilus.userservice.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import net.corilus.userservice.dto.Container;
import net.corilus.userservice.dto.Storage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@Slf4j
public class AzureStorageServiceImpl implements AzureStorageService{
    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Override
    public void createContainer(Container container) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container.getNameContainer());
        containerClient.create();

    }

    @Override
    public void deleteContainer(String nameContainer) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        blobServiceClient.deleteBlobContainer(nameContainer);
    }
}
