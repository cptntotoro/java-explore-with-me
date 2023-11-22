package ru.practicum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// AuthenticationProvider interface is an authentication provider for authenticating users
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    // It requires a UserDetailsService implementation to obtain user information and passwords and
    // then uses PasswordEncoder for password verificationIt requires a UserDetailsService implementation
    // to obtain user information and passwords and then uses PasswordEncoder for password verification
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
        // An AuthenticationManager can do one of 3 things in its authenticate() method:
        //
        //Return an Authentication (normally with authenticated=true) if it can verify that the input represents a valid principal.
        //
        //Throw an AuthenticationException if it believes that the input represents an invalid principal.
        //
        //Return null if it cannot decide.
        // https://spring.io/guides/topicals/spring-security-architecture/

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = isUserValid(username, password);

        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    userDetails.getAuthorities());
        } else {
            // TODO:
            // Throw an AuthenticationException if it believes that the input represents an invalid principal.
            throw new BadCredentialsException("Incorrect user credentials !!");
        }
    }

    //  extra method to allow the caller to query whether it supports a given Authentication type
    // he Class<?> argument in the supports() method is really Class<? extends Authentication>
    // (it is only ever asked if it supports something that is passed into the authenticate() method)
    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType
                .equals(UsernamePasswordAuthenticationToken.class);
    }
}