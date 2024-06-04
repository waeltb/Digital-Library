package net.corilus.userservice.service;

import lombok.extern.slf4j.Slf4j;


import net.corilus.userservice.dto.Role;
import net.corilus.userservice.securityconfig.KeycloakConfig;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Override
    public List<Role> getRoles() {
        Keycloak k = KeycloakConfig.getInstance();
        List<RoleRepresentation> roleRepresentations = k.realm("pfe").roles().list();
        return mapRoles(roleRepresentations);
    }

    @Override
    public Role getRole(String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        return mapRole(k.realm("corilus").roles().get(roleName).toRepresentation());
    }

    @Override
    public void createRole(Role role) {
        RoleRepresentation roleRepresentation=mapRoleRep(role);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("corilus").roles().create(roleRepresentation);
        System.out.println("Role created");
    }

    @Override
    public void updateRole(Role role, String roleName) {
        RoleRepresentation roleRepresentation=mapRoleRep(role);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("corilus").roles().get(roleName).update(roleRepresentation);
    }

    @Override
    public void deleteRole(String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("corilus").roles().deleteRole(roleName);
    }

    @Override
    public void assignRole(String userId, String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        UserResource userResource =k.realm("corilus").users().get(userId);
        RolesResource rolesResource=k.realm("corilus").roles();
        RoleRepresentation representation =rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
    }
    private RoleRepresentation mapRoleRep(Role role){
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.getName());
        return roleRep;

    }
    private List<Role> mapRoles(List<RoleRepresentation> roleRepresentations){
        List<Role>roles = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(roleRepresentations)){
            roleRepresentations.forEach(roleRep->roles.add(mapRole(roleRep)));
        }
        return roles;
    }
    private Role mapRole(RoleRepresentation roleRep){
        Role role = new Role();
        role.setId(roleRep.getId());
        role.setName(roleRep.getName());
        return role ;
    }
}