package net.corilus.courseservice.modal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
public class Speciality {
    private int id ;
    private String name;
    private List<User> users ;
}
