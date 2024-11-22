package ru.practicum.main_svc.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {

    private long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
