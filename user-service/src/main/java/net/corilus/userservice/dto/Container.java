package net.corilus.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Container {
    private String nameContainer;

    public String getNameContainer() {
        return nameContainer;
    }

    public void setNameContainer(String nameContainer) {
        this.nameContainer = nameContainer;
    }
}