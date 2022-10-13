package dev.team.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.team.oauth.clients.UserFeignClient;
import dev.team.usercommons.models.entity.User;

@Service
public class UserService implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = client.findByName(username);

        if (user == null) {

            String message = "Error en el login, usuario no existe " + username;
            log.error(message);

            throw new UsernameNotFoundException(message);
        }

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .peek(authority -> log.info("role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        log.info("User: ", username);

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                user.getIsEnabled(), true, true, true,
                authorities);
    }

}
