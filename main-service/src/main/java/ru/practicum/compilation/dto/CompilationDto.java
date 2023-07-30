package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Integer id;

    private Set<EventShortDto> events;

    private Boolean pinned;

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
