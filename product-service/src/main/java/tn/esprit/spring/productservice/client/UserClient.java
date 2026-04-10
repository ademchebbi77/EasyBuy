package tn.esprit.spring.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.spring.productservice.dto.UserDto;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}