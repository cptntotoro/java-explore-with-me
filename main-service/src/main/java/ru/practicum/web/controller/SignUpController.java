package ru.practicum.web.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final UserService userService;

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        log.info("Calling GET: /sign-up");
        model.addAttribute("user", new NewUserDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createUserAndLogin(@ModelAttribute("user") @Valid NewUserDto user,
                                     BindingResult bindingResult, Model model) {
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        System.out.println(roles);

        return "index";
    }
}