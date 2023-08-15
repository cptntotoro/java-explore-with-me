//package ru.practicum.security.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.user.service.UserService;
//
//@RestController
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//@Slf4j
//public class AdminController {
//
//    private final UserService userService;
//
//    @GetMapping
//    public String userList(Model model) {
//        model.addAttribute("allUsers", userService.getAll());
//        return "admin";
//    }
//
//    // TODO
//    // Надеюсь, это была опечатка: метод Post для удаления...
//    @DeleteMapping
//    public String deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
//                             @RequestParam(required = true, defaultValue = "" ) String action,
//                             Model model) {
//
//        // Метод deleteUser() использует аннотацию RequestParam
//        // т.е. в представлении будет форма, которая должная передать два параметра – userId и action.
//        // Ссылка будет иметь вид http://localhost:8080/admin?userId=24&action=delete
//        // при выполнении такого запроса будет удален пользователь с id=24.
//        if (action.equals("delete")) {
//            userService.delete(userId);
//        }
//        return "redirect:/admin";
//    }
//
//    // TODO
//    // getUser по id?
//    @GetMapping("/gt/{userId}")
//    public String getUsersWithIdBiggerThan(@PathVariable("userId") Long userId, Model model) {
//        // после перехода выведется список всех пользователей с id>24
//        model.addAttribute("allUsers", userService.getUsersWithIdBiggerThan(userId));
//        return "admin";
//    }
//
//
//}
