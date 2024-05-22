package net.corilus.userservice.controller;



import net.corilus.userservice.dto.Role;
import net.corilus.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/getAllRole")
    public List<Role> getRoles(){
        return roleService.getRoles();
    }
    @GetMapping("/getRole/{roleName}")
    public Role getRole(@PathVariable("roleName") String roleName){
        return roleService.getRole(roleName);
    }
    @PostMapping("/addRole")
    public String createRole(@RequestBody Role role){
        roleService.createRole(role);
        return "ajout avec succes";
    }
    @PutMapping("/updateRole/{roleName}")
    public String updateRole(@RequestBody Role role ,@PathVariable("roleName") String roleName){
        roleService.updateRole(role,roleName);
        return "modifier avec succes";
    }
    @DeleteMapping("/deleteRole/{roleName}")
    public String deleteRole(@PathVariable("roleName") String roleName){
        roleService.deleteRole(roleName);
        return "supprimer avec succes";
    }
}

