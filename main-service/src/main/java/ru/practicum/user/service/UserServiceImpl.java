package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.SQLConstraintViolationException;
import ru.practicum.security.repository.RoleRepository;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    // TODO: Метод для Security. Мб UserDto заменить
    @Override
    public UserDto addAdminUser(NewUserRequestDto newUserRequestDto) throws SQLConstraintViolationException {
        User user = userMapper.newUserRequestDtoToUser(newUserRequestDto);

//        Role role = roleRepository.getByName("ROLE_USER").orElseThrow(() -> {
//            throw new ObjectNotFoundException("User role with name = 'ROLE_USER' doesn't exist.");
//        });

//        user.setRoles(Collections.singleton(role));
//        user.setPassword(bCryptPasswordEncoder.encode(newUserRequestDto.getPassword()));

//        User userDtoToSave;

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new SQLConstraintViolationException("User name and/or email already exists.");
        }

        return userMapper.userToUserDto(user);
    }

// Старый код
//    @Override
//    public UserDto addAdminUser(NewUserRequestDto newUserRequestDto) {
//        User user = userMapper.userDtoToUser(newUserRequestDto);
//
//        User userDtoToSave;
//
//        try {
//            userDtoToSave = userRepository.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new SQLConstraintViolationException("User name and/or email already exists.");
//        }
//
//        return userMapper.userToUserDto(userDtoToSave);
//    }

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

    // TODO: Метод для Security
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ObjectNotFoundException("User with username = " + username + " doesn't exist."));
//    }

    // TODO: Метод для Security
    @Override
    public List<User> getUsersWithIdBiggerThan(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
