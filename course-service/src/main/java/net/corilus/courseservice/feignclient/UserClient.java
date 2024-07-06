package net.corilus.courseservice.feignclient;

import net.corilus.courseservice.modal.Speciality;
import net.corilus.courseservice.modal.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="user-service", url="localhost:1998")
public interface UserClient {
    @GetMapping(value="/speciality/getSpecialities")
    List<Speciality> getSpeciality();
    @GetMapping(value="/user/availableexperts/{specialityName}")
    List<User> getAvailableExperts(@PathVariable String specialityName);
}
