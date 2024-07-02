package net.corilus.userservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUserDto.class)
public interface UserDto {

     String firstName();
     String lastName();
     String email();
     String username();
     String password();
     String mobileNumber();






}