package net.corilus.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Valid

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @NotBlank(message = "firstName is required and cannot be blank.")
    @Size(min=3,max = 25,message = "firstName length min is 3 and max is 25")
    private String firstName;
    @NotBlank(message = "lastName is required and cannot be blank.")
    @Size(min=3,max = 25,message = "lastName  length min is 3 and max is 25")
    private String lastName;
    @Email(message = "inavalid mail format")
    @NotBlank(message = "email is required and cannot be blank.")
    private String email;
    @NotBlank(message = " username is required and cannot be blank.")
    @Size(min=3,max = 25,message = " username length min is 3 and max is 25")
    private String username;
    @Size(min = 8,max = 22,message = "password should be min 8 caracters and 22 caracters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one number.")
    private String password;
    private String mobileNumber;
    private Date availabilityDate ;
    @JsonIgnore
    @ManyToOne
    private Role role ;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Leave> leaveList ;
    @JsonIgnore
    @ManyToOne
    private Speciality speciality ;




}
