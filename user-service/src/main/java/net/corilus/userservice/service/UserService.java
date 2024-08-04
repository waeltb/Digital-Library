package net.corilus.userservice.service;
import net.corilus.userservice.dto.AuthenticationRequest;
import net.corilus.userservice.dto.UserDto;
import net.corilus.userservice.entity.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    String login(AuthenticationRequest authenticationRequest);
    String createUser(UserDto user);
    String createExpert(UserDto userDto,String specialityName);
    void updateUser(UserDto userDto,String userId);
    List<UserDto> getUsers();
    UserDto getUser(String id);
    void deleteUser(String id);
     UserRepresentation mapUserRep(UserDto userDto);
    List<UserDto> mapUsers(List<UserRepresentation> userRepresentations);
    UserDto mapUser(UserRepresentation userRep);
    void forgotPassword(String email);
    List<User> getAvailableExperts(String specialityName);
    void uploadImage(MultipartFile file, String username) throws IOException;
    ResponseEntity<Resource> getImage(String username);

}
