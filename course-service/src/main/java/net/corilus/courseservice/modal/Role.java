package net.corilus.courseservice.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
public class Role {

    private int id ;
    private String name;

    private List<User> users;
}
