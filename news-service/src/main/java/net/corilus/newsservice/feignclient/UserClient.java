package net.corilus.newsservice.feignclient;

import net.corilus.newsservice.modal.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service", url="localhost:1998")
public interface UserClient {
    @GetMapping("/user/getUserById/{idUser}")
    User getUserById(@PathVariable("idUser") Long idUser);
}
