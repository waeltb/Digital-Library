package net.corilus.userservice.service;




import net.corilus.userservice.dto.User;

import java.util.List;

public interface UserService {
    void createUser( User user);
    void updateUser(String id, User user);
    List<User> getUsers();
    User getUser( String id);
    void deleteUser(String id);
}
