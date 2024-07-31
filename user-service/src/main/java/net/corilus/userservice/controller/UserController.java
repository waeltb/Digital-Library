package net.corilus.userservice.controller;



import jakarta.validation.Valid;
import net.corilus.userservice.dto.AuthenticationRequest;
import net.corilus.userservice.dto.UserDto;
import net.corilus.userservice.entity.Country;
import net.corilus.userservice.entity.User;
import net.corilus.userservice.exception.EmailExistsExecption;
import net.corilus.userservice.securityconfig.KeycloakConfig;
import net.corilus.userservice.service.UserServiceImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserServiceImpl userService ;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        String response = userService.login(authenticationRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> adduser(  @RequestBody @Valid UserDto userDto){

        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
        }
        catch (EmailExistsExecption e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);//409
        }
        catch (Exception e) {
            System.out.println("test error in exception");
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/addexpert/{specialityName}")
    public ResponseEntity<?> addexpert(  @RequestBody @Valid UserDto userDto,@PathVariable("specialityName") String specialityName){

        try {
            return new ResponseEntity<>(userService.createExpert(userDto,specialityName), HttpStatus.CREATED);
        }
        catch (EmailExistsExecption e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);//409
        }
        catch (Exception e) {
            System.out.println("test error in exception");
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/updateUser/{userId}")
    public String updateUser( @RequestBody UserDto userDto,@PathVariable("userId") String userId){
        System.out.println("**************************");
        System.out.println("userDTO est "+userDto);
        userService.updateUser(userDto,userId);
        return "modifier avce succes";
    }
    @GetMapping("/getUser/{username}")
    public UserDto getUser(@PathVariable("username") String username){
        return  userService.getUser(username);
    }
    @GetMapping("/getAllUser")
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
        return "supprimer avec succes";
    }
    @PutMapping("/{email}/forgot-password")
    public void forgotPassword(@PathVariable("email") String email){
        userService.forgotPassword(email);
    }
    @GetMapping("/availableexperts/{specialityName}")
    public List<User> getAvailableExperts(@PathVariable String specialityName) {
        return userService.getAvailableExperts(specialityName);
    }
    @GetMapping("/getCountry")
    public ResponseEntity<Country[]> getAllCountry() {
        Country[] country = Country.values();
        return ResponseEntity.ok(country);
    }




}
