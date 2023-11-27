package ru.practicum.web.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String getIndex(Model model) {
        log.info("Calling GET: /");
        User user = new User();
        model.addAttribute("user", user);

        // SecurityContextHolder, в нем содержится информация о текущем контексте безопасности приложения,
        // который включает в себя подробную информацию о пользователе(Principal) работающем в настоящее время с приложением.
        // По умолчанию SecurityContextHolder используетThreadLocal для хранения такой информации,
        // что означает, что контекст безопасности всегда доступен для методов исполняющихся в том же самом потоке.

        // Authentication представляет пользователя (Principal) с точки зрения Spring Security

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(scope -> scope.toString())
                .collect(Collectors.toList());

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        model.addAttribute("authorities", authorities);

        log.info("Роли пользователя: " + roles);

        return "index";
    }
}