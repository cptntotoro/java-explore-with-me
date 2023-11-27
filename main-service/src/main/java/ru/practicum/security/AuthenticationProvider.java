package ru.practicum.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.practicum.exception.UserAuthException;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    private final UserDetailsService userService;

    UserDetails isUserValid(String username, String password) {
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

        UserDetails userDetails = isUserValid(username, password);

        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    userDetails.getAuthorities());
        } else {
            throw new UserAuthException("Incorrect user credentials!");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType
                .equals(UsernamePasswordAuthenticationToken.class);
    }
}