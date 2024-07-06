package net.corilus.userservice.securityconfig;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobStorageConfiguration {

    @Value("${azure.storage.container.useraccount}")
    private String containerUserAccount;
    @Value("${azure.storage.container.image}")
    private String containerImage;
    @Value("${azure.storage.container.video}")
    private String containerVideo;

    @Value("${azure.storage.connection.string}")
    private String connectionString;



    @Bean
    public BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();
    }

    @Bean
    public BlobContainerClient createBlobContainerUserAccount() {
        BlobContainerClient containerClient = getBlobServiceClient()
                .getBlobContainerClient(containerUserAccount);

        // Create the container if it does not exist
        if (!containerClient.exists()) {
            containerClient.create();
        }

        return containerClient;
    }
    @Bean
    public BlobContainerClient createBlobContainerImage() {
        BlobContainerClient containerClient = getBlobServiceClient()
                .getBlobContainerClient(containerImage);

        // Create the container if it does not exist
        if (!containerClient.exists()) {
            containerClient.create();
        }

        return containerClient;
    }
    @Bean
    public BlobContainerClient createBlobContainerVideo() {
        BlobContainerClient containerClient = getBlobServiceClient()
                .getBlobContainerClient(containerVideo);

        // Create the container if it does not exist
        if (!containerClient.exists()) {
            containerClient.create();
        }

        return containerClient;
    }
}