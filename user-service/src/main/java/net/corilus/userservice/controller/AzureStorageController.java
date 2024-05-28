package net.corilus.userservice.controller;



import net.corilus.userservice.dto.Container;
import net.corilus.userservice.service.AzureStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/azure")
public class AzureStorageController {
    @Autowired
    AzureStorageServiceImpl azureBlobStorage ;
    @PostMapping("/addContainer")
    public String addContainer(@RequestBody Container container){
        azureBlobStorage.createContainer(container);
        return "ajout avec succes";
    }
    @DeleteMapping("/deleteContainer/{nomContainer}")
    public String deleteContainer(@PathVariable("nomContainer") String nameContainer){
        azureBlobStorage.deleteContainer(nameContainer);
        return "supprimer avec succes";
    }
}
