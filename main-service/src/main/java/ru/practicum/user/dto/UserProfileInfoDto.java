package ru.practicum.user.dto;

import ru.practicum.user.model.User;

public class UserProfileInfoDto extends User {
    private String email;
    private String name;
    private String username;

    public UserProfileInfoDto(String email, String name, String username) {
        this.email = email;
        this.name = name;
        this.username = username;
    }
}
