package ru.practicum.web.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserLoginDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

//    private final AuthenticationManager authenticationManager;

//    private final JwtUtils jwtUtils;

    @GetMapping("/login")
    public String getLogin(Model model) {
        log.info("Calling GET: /login");
        model.addAttribute("user", new NewUserRequestDto());
        return "sign-in";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") @Valid UserLoginDto userLoginDto,
//                                     BindingResult bindingResult, Model model) {
//        log.info("Calling POST: /login");
//
////        Authentication authentication = authenticationManager
////                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
//
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////
////        User user = (User) authentication.getPrincipal();
//
////        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
//
//        model.addAttribute("user", userLoginDto);
//
//        // вывести роль
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        Set<String> roles = auth.getAuthorities().stream()
//                .map(r -> r.getAuthority()).collect(Collectors.toSet());
//
//        System.out.println(roles);
//
//        return "index";
//    }
}
