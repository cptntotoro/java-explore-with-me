package ru.practicum.web.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserLoginDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class SignInController {

    private final UserService userService;

    @GetMapping("/sign-in")
    public String getLogin(Model model) {
        log.info("Calling GET: /sign-in");
        model.addAttribute("user", new NewUserDto());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());

        System.out.println(roles);

        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute("user") @Valid UserLoginDto userLoginDto, Model model) {
        log.info("Calling POST: /sign-in");

        model.addAttribute("user", userLoginDto);

        // вывести роль
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = auth.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());

        System.out.println(roles);

        return "index";
    }
}
