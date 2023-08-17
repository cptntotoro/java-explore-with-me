//package ru.practicum.web.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import ru.practicum.event.dto.NewEventDto;
//import ru.practicum.event.service.EventService;
//
//@Controller
//@RequestMapping("/events")
//@RequiredArgsConstructor
//public class WebEventController {
//
//    private EventService eventService;
//
//    @GetMapping
//    public String newEventPage(Model model) {
//        // В GET запросе на страницу добавляется новый пустой объект класса NewUserRequestDto.
//        // Это сделано для того, чтобы при POST запросе не доставать данные из формы регистрации по одному, а сразу получить заполненный объект userForm.
//        model.addAttribute("event", new NewEventDto());
//        return "add-event";
//    }
//
//
////    @PostMapping
////    @ResponseStatus(HttpStatus.CREATED)
////    public EventFullDto addUserEvent(@PathVariable Long userId,
////                                     @RequestBody @Valid NewEventDto event) {
////
////        return eventService.addUserEvent(userId, event);
////    }
//
//}
