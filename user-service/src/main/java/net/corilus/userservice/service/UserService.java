package net.corilus.userservice.service;
import net.corilus.userservice.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserService {
    String createUser(UserDto user);
    void updateUser(String id, UserDto user);
    List<UserDto> getUsers();
    UserDto getUser(String id);
    void deleteUser(String id);
     UserRepresentation mapUserRep(UserDto userDto);
    List<UserDto> mapUsers(List<UserRepresentation> userRepresentations);
    UserDto mapUser(UserRepresentation userRep);
    void forgotPassword(String email);
}
