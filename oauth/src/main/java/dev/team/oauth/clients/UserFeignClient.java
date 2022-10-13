package dev.team.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.team.usercommons.models.entity.User;

/**
 * UserFeignClient
 */
@FeignClient("ms-users")
public interface UserFeignClient {
    @GetMapping("/users/search/by-name")
    public User findByName(@RequestParam String name);

}