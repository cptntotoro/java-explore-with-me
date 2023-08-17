package ru.practicum.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

// UserDetailsService, используется чтобы создать UserDetails объект путем реализации единственного метода этого интерфейса
public interface UserService extends UserDetailsService {
    // TODO: Метод для Security. Мб UserDto заменить
    List<UserDto> getAll();

    List<UserDto> getByIds(List<Long> ids, Integer from, Integer size);

    UserDto addAdminUser(NewUserRequestDto body);

    void delete(Long userId);

    // TODO: Метод для Security
    List<User> getUsersWithIdBiggerThan(Long idMin);

    UserDto add(NewUserRequestDto user);

    User findByEmail(String email) throws UsernameNotFoundException;
}
