package net.corilus.userservice.service;


import lombok.extern.slf4j.Slf4j;

import net.corilus.userservice.dto.User;
import net.corilus.userservice.securityconfig.KeycloakConfig;
import org.keycloak.common.util.CollectionUtil;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.admin.client.Keycloak;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public void createUser(User user) {
        UserRepresentation userRep= mapUserRep(user);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").users().create(userRep);

    }

    @Override
    public void updateUser(String id, User user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").users().get(id).update(userRep);

    }

    @Override
    public List<User> getUsers() {
        Keycloak k = KeycloakConfig.getInstance();
        List<UserRepresentation> userRepresentations=k.realm("pfe").users().list();
        return mapUsers(userRepresentations);
    }

    @Override
    public User getUser(String id) {
        Keycloak k = KeycloakConfig.getInstance();
        return mapUser(k.realm("pfe").users().get(id).toRepresentation());
    }

    @Override
    public void deleteUser(String id) {
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").users().delete(id);
    }
    private UserRepresentation mapUserRep(User user){
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep ;
    }
    private List<User> mapUsers(List<UserRepresentation> userRepresentations){
        List<User> users= new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)){
            userRepresentations.forEach(userRep->{
                users.add(mapUser(userRep));
            });
        }
        return users;
    }
    private User mapUser(UserRepresentation userRep){
        User user=new User();
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUserName(userRep.getUsername());
        return user;
    }
}

