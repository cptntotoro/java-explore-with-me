package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.user.model.Role;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.RoleRepository;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    // TODO: Метод для Security. Мб UserDto заменить
    // saveUser
    @Override
    public UserDto addAdminUser(NewUserDto newUserDto) throws SQLConstraintViolationException {
//        User userFromDB = userRepository.findByUsername(newUserRequestDto.getUsername()).orElseThrow(() -> {
//            throw new IncorrectRequestException("User with username = " + newUserRequestDto.getUsername() + " already exists.");
//        });

        User user = userMapper.newUserRequestDtoToUser(newUserDto);

        Role role = roleRepository.getByName("ROLE_USER").orElseThrow(() -> {
            throw new ObjectNotFoundException("Role with name = 'USER' doesn't exist.");
        });

        user.setRoles(Collections.singleton(role));
        user.setPassword(newUserDto.getPassword());

//        try {
            user = userRepository.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new SQLConstraintViolationException("User name and/or email already exists.");
//        }

        return userMapper.userToUserDto(user);
    }

    // TODO: Метод для Security. Мб UserDto заменить
    public UserDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + userId + "doesn't exist."));
        return userMapper.userToUserDto(user);
    }

    // TODO: Метод для Security. Мб UserDto заменить
    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getByIds(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, pageable).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void delete(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("User with id = " + userId + " was not found.");
        }
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email = " + email + " doesn't exist."));
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User with username = " + username + " doesn't exist."));
    }


    // TODO: Метод для Security
    @Override
    public List<User> getUsersWithIdBiggerThan(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }

    @Override
    public UserDto add(NewUserDto newUser) throws SQLConstraintViolationException {
        User user = userMapper.newUserRequestDtoToUser(newUser);

        Role role = roleRepository.getByName("ROLE_USER").orElseThrow(() -> {
            throw new ObjectNotFoundException("Role 'USER' doesn't exist.");
        });

        user.setRoles(Collections.singleton(role)); // user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(newUser.getPassword());

        user = userRepository.save(user);

        log.info("Создан пользователь с id = " + user.getId());

        return userMapper.userToUserDto(user);
    }
}
