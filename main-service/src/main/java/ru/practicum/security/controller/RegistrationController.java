package ru.practicum.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserService userService;

    // Чтобы что-то добавить или получить со страницы мы обращаемся к model.
    @GetMapping
    public String registration(Model model) {
        // В GET запросе на страницу добавляется новый пустой объект класса NewUserRequestDto.
        // Это сделано для того, чтобы при POST запросе не доставать данные из формы регистрации по одному, а сразу получить заполненный объект userForm.
        model.addAttribute("user", new NewUserRequestDto());
        return "registration";
    }

    @PostMapping
//    public String addUser(@ModelAttribute("user") @Valid NewUserRequestDto userForm,
    public String addUser(@ModelAttribute("user") @Valid NewUserRequestDto user,
                          BindingResult bindingResult, Model model) {
//                          BindingResult bindingResult) {

        // Метод addUser() в качестве параметров ожидает объект пользователя (userForm),
        // который был добавлен при GET запросе.
        // Аннотация Valid проверяет выполняются ли ограничения, установленные на поля.
        // Если ограничения не были выполнены, то bindingResult будет содержать ошибки.

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // Если пароль и его подтверждение не совпадают добавляем сообщение на страницу и возвращаем её.
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return "registration";
        }

        // Метод saveUser() возвращает false, если пользователь с таким именем уже существует и true,
        // если пользователь сохранен в БД.
        // При неудачной попытке сохранения – добавляем сообщение об ошибке и возвращаем страницу.
        // При удачном сохранении пользователя – переходим на главную страницу.
//        if (!userService.saveUser(userForm)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "registration";
//        }

        // В конце пробуем сохранить добавить пользователя в БД.
        // TODO Тут я не уверена

        UserDto userDto;

        try {
            userDto = userService.addAdminUser(user);
        } catch (SQLConstraintViolationException e) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "registration";
        }
//
//        model.addAttribute("userId", userDto.getId());
//        model.addAttribute("userName", userDto.getName());
//        model.addAttribute("userEmail", userDto.getEmail());
        model.addAttribute("user", userDto);
//        return "redirect:/"; //return "index";
        return "success";
    }
}
