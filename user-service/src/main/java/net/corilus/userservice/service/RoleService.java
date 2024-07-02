package net.corilus.userservice.service;




import net.corilus.userservice.dto.RoleDto;

import java.util.List;

public interface RoleService {


    List<RoleDto> getRoles();

    RoleDto getRole( String roleName);

    void createRole( RoleDto role);

    void updateRole(RoleDto role ,String roleName);

    void deleteRole(String roleName);

    void assignRole( String userId, String roleName);
}
