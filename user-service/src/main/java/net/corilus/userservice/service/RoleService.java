package net.corilus.userservice.service;



import net.corilus.userservice.dto.Role;

import java.util.List;

public interface RoleService {


    List<Role> getRoles();

    Role getRole( String roleName);

    void createRole( Role role);

    void updateRole(Role role ,String roleName);

    void deleteRole(String roleName);

    void assignRole( String userId, String roleName);
}
