package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @NotNull
    @Size(min = 5)
    private String username;

    @NotNull
    @Size(min = 5)
    private String password;
}