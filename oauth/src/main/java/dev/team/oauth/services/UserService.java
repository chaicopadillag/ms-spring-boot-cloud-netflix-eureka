package dev.team.oauth.services;

import java.lang.System.Logger;
import java.util.List;
import java.util.stream.Collectors;

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
import feign.FeignException;

@Service
public class UserService implements IUserService, UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient client;

    // @Autowired
    // private Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            User user = client.findByName(username);

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(authority -> log.info("role: " + authority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("User: ", username);

            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                    user.getIsEnabled(), true, true, true,
                    authorities);
        } catch (FeignException e) {
            String message = "Error en el login, usuario no existe " + username;
            log.error(message);

            throw new UsernameNotFoundException(message);
        }
    }

    @Override
    public User findByName(String name) {
        return client.findByName(name);
    }

    @Override
    public User update(User user, Long id) {

        return client.update(user, id);
    }

}
