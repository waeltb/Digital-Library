package net.corilus.userservice.securityconfig;

import net.corilus.userservice.entity.Role;
import net.corilus.userservice.entity.Speciality;
import net.corilus.userservice.repository.RoleRepository;
import net.corilus.userservice.repository.SpecialityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, SpecialityRepository specialityRepository) {
        return args -> {
            // Load roles if they don't exist
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(1, "admin", null));
                roleRepository.save(new Role(2, "user", null));
                roleRepository.save(new Role(3, "expert", null));
            }

            // Load specialties if they don't exist
            if (specialityRepository.count() == 0) {
                specialityRepository.save(new Speciality(1, "Médecine générale", null));
                specialityRepository.save(new Speciality(2, "Cardiologie", null));
                specialityRepository.save(new Speciality(3, "Dermatologie", null));
                specialityRepository.save(new Speciality(4, "Gastro-entérologie", null));
                specialityRepository.save(new Speciality(5, "Hématologie", null));
                specialityRepository.save(new Speciality(6, "Neurologie", null));
                specialityRepository.save(new Speciality(7, "Oncologie", null));
                specialityRepository.save(new Speciality(8, "Pédiatrie", null));
                specialityRepository.save(new Speciality(9, "Psychiatrie", null));
            }
        };
    }
}
