package ru.practicum;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutputDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private Long hits;
}