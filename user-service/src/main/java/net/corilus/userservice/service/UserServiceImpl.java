package net.corilus.userservice.service;


import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.corilus.userservice.dto.UserDto;
import net.corilus.userservice.exception.EmailExistsExecption;

import net.corilus.userservice.securityconfig.KeycloakConfig;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;


import org.keycloak.admin.client.Keycloak;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    @Override
    public String createUser(UserDto userDto) {
        try {
            UserRepresentation userRep= mapUserRep(userDto);
            Keycloak keycloak = KeycloakConfig.getInstance();
            List<UserRepresentation> usernameRepresentations = keycloak.realm("pfe").users().searchByUsername(userDto.getUsername(),true);
            List<UserRepresentation> emailRepresentations = keycloak.realm("pfe").users().searchByEmail(userDto.getEmail(),true);

            if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
                throw new EmailExistsExecption("username or email already exists");
            }
            Response response = keycloak.realm("pfe").users().create(userRep);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user");
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            UserResource userResource = keycloak.realm("pfe").users().get(userId);
            userResource.sendVerifyEmail();
            return "User created";

        }
        catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }


       /* if(!CollectionUtils.isEmpty(representationList)){
            UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation -> Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
            assert userRepresentation1 != null;
            emailverification(userRepresentation1.getId());
        }*/


    }

    @Override
    public void updateUser(String id, UserDto user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").users().get(id).update(userRep);

    }

    @Override
    public List<UserDto> getUsers() {
        Keycloak k = KeycloakConfig.getInstance();
        List<UserRepresentation> userRepresentations=k.realm("pfe").users().list();
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
    private UserRepresentation mapUserRep(UserDto userDto){

            UserRepresentation userRep = new UserRepresentation();
            userRep.setUsername(userDto.getUsername());
            userRep.setFirstName(userDto.getFirstName());
            userRep.setLastName(userDto.getLastName());
            userRep.setEmail(userDto.getEmail());
            userRep.setEnabled(true);
            userRep.setEmailVerified(false);
            List<CredentialRepresentation> creds = new ArrayList<>();
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setValue(userDto.getPassword());
            creds.add(cred);
            userRep.setCredentials(creds);


            return userRep ;





    }
    private List<UserDto> mapUsers(List<UserRepresentation> userRepresentations){
        List<UserDto> users= new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)){
            userRepresentations.forEach(userRep->{
                users.add(mapUser(userRep));
            });
        }
        return users;
    }
    private UserDto mapUser(UserRepresentation userRep){
        UserDto user=new UserDto();
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUsername(userRep.getUsername());
        return user;
    }
    @Override
    public void emailverification(String userId){
        Keycloak k = KeycloakConfig.getInstance();
      UsersResource usersResource=  k.realm("pfe").users();
      usersResource.get(userId).sendVerifyEmail();
    }




}

