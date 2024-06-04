package net.corilus.userservice.service;





import net.corilus.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    String createUser(UserDto user);
    void updateUser(String id, UserDto user);
    List<UserDto> getUsers();
    UserDto getUser(String id);
    void deleteUser(String id);
    void emailverification(String userId);
}
