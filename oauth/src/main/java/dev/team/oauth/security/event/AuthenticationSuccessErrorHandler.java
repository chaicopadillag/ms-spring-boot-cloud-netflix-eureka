package dev.team.oauth.security.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import dev.team.oauth.services.IUserService;
import dev.team.usercommons.models.entity.User;
import feign.FeignException;

/**
 * AuthenticationSuccessErrorHandler
 */
@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private IUserService userService;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {

        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            // if(authentication.getName().equals("frontendapp")){
            return;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();

        System.out.println("Success login " + user.getUsername());

        User authUser = userService.findByName(authentication.getName());

        if (authUser.getIntents() != null && authUser.getIntents() > 0) {
            authUser.setIntents(0);

            userService.update(authUser, authUser.getId());
        }

    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        System.out.println("Error en el login: " + exception.getMessage());

        try {

            User user = userService.findByName(authentication.getName());

            if (user.getIntents() == null) {
                user.setIntents(0);
            }

            user.setIntents(user.getIntents() + 1);

            if (user.getIntents() >= 3) {
                user.setIsEnabled(false);
            }

            userService.update(user, user.getId());

        } catch (FeignException e) {
            System.out.println("El usuario no existe en sistema");
        }

    }

}