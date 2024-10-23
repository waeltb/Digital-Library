package net.corilus.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.corilus.userservice.dto.ImmutableRoleDto;
import net.corilus.userservice.dto.RoleDto;
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
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Override
    public List<RoleDto> getRoles() {
        Keycloak k = KeycloakConfig.getInstance();
        List<RoleRepresentation> roleRepresentations = k.realm("pfe").roles().list();
        return mapRoles(roleRepresentations);
    }

    @Override
    public RoleDto getRole(String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        return mapRole(k.realm("pfe").roles().get(roleName).toRepresentation());
    }

    @Override
    public void createRole(RoleDto role) {
        RoleRepresentation roleRepresentation=mapRoleRep(role);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").roles().create(roleRepresentation);
        System.out.println("Role created");
    }

    @Override
    public void updateRole(RoleDto role, String roleName) {
        RoleRepresentation roleRepresentation=mapRoleRep(role);
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").roles().get(roleName).update(roleRepresentation);
    }

    @Override
    public void deleteRole(String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        k.realm("pfe").roles().deleteRole(roleName);
    }

    @Override
    public void assignRole(String userId, String roleName) {
        Keycloak k = KeycloakConfig.getInstance();
        UserResource userResource =k.realm("pfe").users().get(userId);
        RolesResource rolesResource=k.realm("pfe").roles();
        RoleRepresentation representation =rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
    }
    private RoleRepresentation mapRoleRep(RoleDto role){
        RoleRepresentation roleRep = new RoleRepresentation();
        roleRep.setName(role.name());
        return roleRep;

    }
    private List<RoleDto> mapRoles(List<RoleRepresentation> roleRepresentations){
        List<RoleDto>roles = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(roleRepresentations)){
            roleRepresentations.forEach(roleRep->roles.add(mapRole(roleRep)));
        }
        return roles;
    }
    private RoleDto mapRole(RoleRepresentation roleRep){
        return ImmutableRoleDto.builder()
                .id(roleRep.getId())
                .name(roleRep.getName())
                .build();
    }
}