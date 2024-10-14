package net.corilus.userservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Date;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableUserDto.class)
public interface UserDto {

     String lastName();
     String email();
     String username();
     String password();
     String mobileNumber();
     Optional<Date> availabilityDate();





}