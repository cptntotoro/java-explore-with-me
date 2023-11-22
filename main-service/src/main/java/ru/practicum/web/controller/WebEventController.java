package ru.practicum.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users/add-event")
@RequiredArgsConstructor
@Slf4j
public class WebEventController {

    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping
    public String newEventPage(Model model) {
        log.info("Calling GET: /users/add-event");
        model.addAttribute("event", new NewEventDto());

        log.info("Calling GET: /categories");
        List<CategoryDto> selectCategoryOptions = categoryService.getAll(0, 10);
        model.addAttribute("selectCategoryOptions", selectCategoryOptions);

        return "add-event";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addUserEvent(@ModelAttribute("event") @RequestBody @Valid NewEventDto event) {
        log.info("Calling POST: /users/add-event");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userService.loadUserByUsername(authentication.getName());

        EventFullDto savedEvent = eventService.addUserEvent(currentUser.getId(), event);

        log.info(savedEvent.toString());

        return "index";
    }
}
