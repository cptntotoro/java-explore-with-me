package ru.practicum.web.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserLoginDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        log.info("Calling GET: /sign-up");
        model.addAttribute("user", new NewUserDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createUserAndLogin(@ModelAttribute("user") @Valid NewUserDto user, Model model) {
        log.info("Calling POST: /sign-up");

        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("errorMessage", "Passwords do not match."); // "passwordError"
            return "index";
        }

        UserDto userSaved;

        try {
            userSaved = userService.add(user);
        } catch (SQLConstraintViolationException e) {
            model.addAttribute("errorMessage", "User with this name and/or email already exists.");
            return "sign-up";
        }

        model.addAttribute("user", userSaved);

        logPrincipalRoles();

        return "index";
    }

    @GetMapping("/sign-in")
    public String getLogin(Model model) {
        log.info("Calling GET: /sign-in");
        model.addAttribute("user", new NewUserDto());

        logPrincipalRoles();

        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute("user") @Valid UserLoginDto userLoginDto, Model model) {
        log.info("Calling POST: /sign-in");

        model.addAttribute("user", userLoginDto);

        logPrincipalRoles();

        return "index";
    }

    private void logPrincipalRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        log.info("Роли пользователя: " + roles);
    }
}
