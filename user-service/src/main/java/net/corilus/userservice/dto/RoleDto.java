package net.corilus.userservice.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface RoleDto {

    String id();
    String name();
}
