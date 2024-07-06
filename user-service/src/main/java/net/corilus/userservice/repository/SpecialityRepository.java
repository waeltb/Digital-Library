package net.corilus.userservice.repository;

import net.corilus.userservice.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    void deleteSpecialityByName(String name);
    Speciality findByName(String name);

}
