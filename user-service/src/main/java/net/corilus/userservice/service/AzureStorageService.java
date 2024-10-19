package net.corilus.userservice.service;


import net.corilus.userservice.dto.Container;
import net.corilus.userservice.dto.Storage;

import java.util.List;

public interface AzureStorageService {


    void createContainer(Container conntaier) ;

    void deleteContainer(String nameContainer) ;
}
