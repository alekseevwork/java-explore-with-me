package ru.practicum.main_svc.category;

import ru.practicum.main_svc.category.dto.CategoryDto;
import ru.practicum.main_svc.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto dto);

    void delete(Long id);

    CategoryDto update(Long categoryId, CategoryDto dto);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(Long id);
}
