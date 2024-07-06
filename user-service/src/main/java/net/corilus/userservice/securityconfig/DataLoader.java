package net.corilus.userservice.securityconfig;

import net.corilus.userservice.entity.Role;
import net.corilus.userservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(1, "admin", null));
                roleRepository.save(new Role(2, "user", null));
                roleRepository.save(new Role(3, "expert", null));
            }
        };
    }
}
