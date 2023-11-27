package ru.practicum.web;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserLoginDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        model.addAttribute("user", new NewUserDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createUserAndLogin(@ModelAttribute("user") @Valid NewUserDto user, Model model) {

        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("errorMessage", "Passwords do not match.");
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
        return "index";
    }

    @GetMapping("/sign-in")
    public String getLogin(Model model) {
        model.addAttribute("user", new NewUserDto());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute("user") @Valid UserLoginDto userLoginDto, Model model) {
        model.addAttribute("user", userLoginDto);
        return "index";
    }
}