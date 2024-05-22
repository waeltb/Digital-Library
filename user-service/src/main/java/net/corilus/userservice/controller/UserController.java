package net.corilus.userservice.controller;



import net.corilus.userservice.dto.User;
import net.corilus.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    UserServiceImpl userService ;
    @PostMapping("/addUser")
    public String adduser(@RequestBody User user){
        userService.createUser(user);
        return "ajouter avec succes";
    }
    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") String id,@RequestBody User user){
        userService.updateUser(id,user);
        return "modifier avce succes";
    }
    @GetMapping("/getUser/{id}")
    public User getUser(@PathVariable("id") String id){
        return  userService.getUser(id);
    }
    @GetMapping("/getAllUser")
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
        return "supprimer avec succes";
    }
}
