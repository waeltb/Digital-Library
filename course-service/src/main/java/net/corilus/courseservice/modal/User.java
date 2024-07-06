package net.corilus.courseservice.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {


    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;
    private String password;
    private String mobileNumber;
    private Date availabilityDate;

    private Role role;

    private List<Leave> leaveList;

    private Speciality speciality;

}