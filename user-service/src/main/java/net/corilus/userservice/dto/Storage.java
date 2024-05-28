package net.corilus.userservice.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.Base64;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    private String path;
    private String fileName;
    private byte[] fileContent;

    public void setFileContentBase64(String base64String) {
        this.fileContent = Base64.getDecoder().decode(base64String);
    }

    public String getFileContentBase64() {
        return Base64.getEncoder().encodeToString(this.fileContent);
    }
}

