package ru.practicum.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAll();

    List<User> getByIds(List<Long> ids, Integer from, Integer size);

    User addAdminUser(User user);

    void delete(Long userId);

    List<User> getUsersWithIdBiggerThan(Long idMin);

    User add(NewUserDto user);

    User loadUserByUsername(String username) throws UsernameNotFoundException;
}
