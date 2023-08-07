package ru.practicum.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends NewCategoryDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Long id;

    public CategoryDto(@NotBlank @Length(max = 50) String name, Long id) {
        super(name);
        this.id = id;
    }
}
