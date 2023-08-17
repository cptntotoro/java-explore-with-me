package ru.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.practicum.user.service.UserService;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CustomIdentityAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userService;

    UserDetails isValidUser(String username, String password) {
        UserDetails user = userService.loadUserByUsername(username);
        if (username.equalsIgnoreCase(user.getUsername())
                && password.equals(user.getPassword())) {
            UserDetails userDetails = User
                    .withUsername(username)
                    .password("NOT_DISCLOSED")
                    .authorities(((ru.practicum.user.model.User) user).getRoles())
                    .build();
            return userDetails;
        }
        return null;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = isValidUser(username, password);

        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Incorrect user credentials !!");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType
                .equals(UsernamePasswordAuthenticationToken.class);
    }
}