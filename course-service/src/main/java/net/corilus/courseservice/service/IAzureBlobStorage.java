package net.corilus.courseservice.service;

import net.corilus.courseservice.dto.Container;

public interface IAzureBlobStorage {
    void createContainer(Container container) ;

    void deleteContainer(String nameContainer) ;
}
