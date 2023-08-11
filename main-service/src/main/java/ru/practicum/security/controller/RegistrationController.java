package ru.practicum.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserService userService;

    // Чтобы что-то добавить или получить со страницы мы обращаемся к model.

    @PostMapping
    public String addUser(@ModelAttribute("userForm") @Valid NewUserRequestDto userForm,
                          BindingResult bindingResult, Model model) {

        // Метод addUser() в качестве параметров ожидает объект пользователя (userForm),
        // который был добавлен при GET запросе.
        // Аннотация Valid проверяет выполняются ли ограничения,
        // установленные на поля, в данном случае длина не меньше 2 символов.
        // Если ограничения не были выполнены, то bindingResult будет содержать ошибки.

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // Если пароль и его подтверждение не совпадают добавляем сообщение на страницу и возвращаем её.
        // В конце пробуем сохранить добавить пользователя в БД.
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }

        // Метод saveUser() возвращает false, если пользователь с таким именем уже существует и true,
        // если пользователь сохранен в БД.
        // При неудачной попытке сохранения – добавляем сообщение об ошибке и возвращаем страницу.
        // При удачном сохранении пользователя – переходим на главную страницу.
//        if (!userService.addAdminUser(userForm)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "registration";
//        }

        // TODO Тут я не уверена
        try {
            userService.addAdminUser(userForm);
        } catch (SQLConstraintViolationException e) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/";
    }


    @GetMapping
    public ModelAndView registerUserAccount(ModelAndView model) {
        // В GET запросе на страницу добавляется новый пустой объект класса User.
        // Это сделано для того, чтобы при POST запросе не доставать данные из формы регистрации по одному
        // (username, password, passwordComfirm), а сразу получить заполненный объект userForm.
        model.setViewName("registration");
//        model.addAttribute("userForm", new NewUserRequestDto());
//        model.addObject("items", itemService.getAll());
        return model;
    }

//    @GetMapping
//    public String registration(Model model) {
//        NewUserRequestDto userDto = new NewUserRequestDto();
//        model.addAttribute("user", userDto);
//        return "registration";
//    }
}
