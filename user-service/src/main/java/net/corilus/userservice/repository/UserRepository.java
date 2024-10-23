package net.corilus.userservice.repository;

import net.corilus.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByRole_NameAndAvailabilityDateLessThanEqualAndSpeciality_NameOrderByAvailabilityDate(String roleName, Date currentDate, String specialityName);
    User findByUsername(String username);

}
