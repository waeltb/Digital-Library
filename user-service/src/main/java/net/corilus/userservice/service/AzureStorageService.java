package net.corilus.userservice.service;


import net.corilus.userservice.dto.Container;
import net.corilus.userservice.dto.Storage;

import java.util.List;

public interface AzureStorageService {
    String write(Storage storage,Container container);
    String update(Storage storage);

    byte[] read(Storage storage) ;

    List<String> listFiles(Storage storage) ;

    void delete(Storage storage) ;

    void createContainer(Container container) ;

    void deleteContainer(String nameContainer) ;
}
