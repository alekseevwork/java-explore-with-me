package ru.practicum.main_svc.category;

import ru.practicum.main_svc.category.dto.CategoryDto;
import ru.practicum.main_svc.category.dto.NewCategoryDto;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
