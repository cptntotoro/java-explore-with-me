package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @NotNull
    @Size(min = 5)
    private String username;

    @NotNull
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private String email;

    @NotNull
    @Size(min = 5)
    private String password;

    @NotNull
    @Transient
    private String passwordConfirm;
}