package ru.practicum.web;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.User;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String getIndex(Model model) {

        User user = new User();
        model.addAttribute("user", user);

        return "index";
    }
}