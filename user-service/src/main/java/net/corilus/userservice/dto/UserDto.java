package net.corilus.userservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.corilus.userservice.entity.Country;
import org.immutables.value.Value;

import java.util.Date;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableUserDto.class)
public interface UserDto {
     String firstName();
     String lastName();
     String email();
     String username();
     Optional<String> password();
     String mobileNumber();
     Optional<Date> availabilityDate();

     Optional<Country> country();  // Ajout du champ Optional<Country>




}