package ru.practicum.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends NewCategoryDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Long id;
}
