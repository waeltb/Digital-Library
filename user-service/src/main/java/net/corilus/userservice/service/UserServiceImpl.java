package net.corilus.userservice.service;


import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.corilus.userservice.dto.UserDto;
import net.corilus.userservice.exception.EmailExistsExecption;
import net.corilus.userservice.securityconfig.KeycloakConfig;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;


import org.keycloak.admin.client.Keycloak;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
@Autowired
     RoleServiceImpl roleService;

    @Override
    public String createUser(UserDto userDto) {
         String user ="user";
        try {
            UserRepresentation userRep= mapUserRep(userDto);
            Keycloak keycloak = KeycloakConfig.getInstance();
            List<UserRepresentation> usernameRepresentations = keycloak.realm("corilus").users().searchByUsername(userDto.getUsername(),true);
            List<UserRepresentation> emailRepresentations = keycloak.realm("corilus").users().searchByEmail(userDto.getEmail(),true);

            if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
                throw new EmailExistsExecption("username or email already exists");
            }
            Response response = keycloak.realm("corilus").users().create(userRep);


            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user");
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            roleService.getRole(user);
            roleService.assignRole(userId,user);
            UserResource userResource = keycloak.realm("corilus").users().get(userId);
            userResource.sendVerifyEmail();
            return "User created";

        }
        catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void updateUser(String id, UserDto user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("corilus").users().get(id).update(userRep);

    }

    @Override
    public List<UserDto> getUsers() {
        Keycloak k = KeycloakConfig.getInstance();
        List<UserRepresentation> userRepresentations=k.realm("corilus").users().list();
        return mapUsers(userRepresentations);
    }

    @Override
    public UserDto getUser(String id) {
        Keycloak k = KeycloakConfig.getInstance();
        return mapUser(k.realm("pfe").users().get(id).toRepresentation());
    }

    @Override
    public void deleteUser(String id) {
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").users().delete(id);
    }
    @Override
    public UserRepresentation mapUserRep(UserDto userDto){

            UserRepresentation userRep = new UserRepresentation();
            userRep.setUsername(userDto.getUsername());
            userRep.setFirstName(userDto.getFirstName());
            userRep.setLastName(userDto.getLastName());
            userRep.setEmail(userDto.getEmail());
            userRep.setEnabled(true);
            userRep.setEmailVerified(false);

        Map<String, List<String>> attributes = new HashMap<>();
        if (userDto.getMobileNumber() != null) {
            List<String> mobileNumber = new ArrayList<>();
            mobileNumber.add(userDto.getMobileNumber());
            attributes.put("mobileNumber", mobileNumber);
        }
        userRep.setAttributes(attributes);

            List<CredentialRepresentation> creds = new ArrayList<>();
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setValue(userDto.getPassword());
            creds.add(cred);
            userRep.setCredentials(creds);
            return userRep ;

    }
    @Override
    public List<UserDto> mapUsers(List<UserRepresentation> userRepresentations){
        List<UserDto> users= new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)){
            userRepresentations.forEach(userRep->{
                users.add(mapUser(userRep));
            });
        }
        return users;
    }
    @Override
    public UserDto mapUser(UserRepresentation userRep){
        UserDto user=new UserDto();
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUsername(userRep.getUsername());
        return user;
    }

@Override
public void forgotPassword(String email) {
    Keycloak keycloak = KeycloakConfig.getInstance();
    List<UserRepresentation> emailRepresentations = keycloak.realm("corilus").users().searchByEmail(email,true);
    System.out.println("********test0******** "+emailRepresentations);
    if (!emailRepresentations.isEmpty()) {
        UserRepresentation userRepresentation = emailRepresentations.get(0); // Get the first user
        System.out.println("********test1******** "+userRepresentation);

        try {
            UserResource userResource = keycloak.realm("corilus").users().get(userRepresentation.getId());
            List<String> actions = new ArrayList<>();
            actions.add("UPDATE_PASSWORD");
            userResource.executeActionsEmail(actions);

            System.out.println("Password reset email sent successfully.");
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
            throw new RuntimeException("Failed to reset password.");
        }
    } else {
        System.err.println("User with username '" + email + "' not found.");
        throw new RuntimeException("User not found.");
    }
}



}

