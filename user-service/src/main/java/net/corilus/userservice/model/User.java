package net.corilus.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    private String firstName;
    private String lastName;
    private String email;
    private String supplierName;
    private String companyName;
    private String password;
    private String mobileNumber;
    private Boolean nonLocked;
    private Boolean enabled;
    private String photoProfile;
    private Collection<? extends GrantedAuthority> authorities;



}
