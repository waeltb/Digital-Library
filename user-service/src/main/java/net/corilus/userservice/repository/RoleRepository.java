package net.corilus.userservice.repository;

import net.corilus.userservice.entity.Role;
import net.corilus.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);


}
