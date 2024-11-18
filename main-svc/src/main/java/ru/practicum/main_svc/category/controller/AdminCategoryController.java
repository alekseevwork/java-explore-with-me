package ru.practicum.main_svc.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.category.CategoryService;
import ru.practicum.main_svc.category.dto.CategoryDto;
import ru.practicum.main_svc.category.dto.NewCategoryDto;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto dto) {
        log.info("POST /admin/categories: create - {}", dto);
        return categoryService.create(dto);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@Valid @RequestBody CategoryDto dto,
                              @PathVariable @Positive Long id) {
        log.info("POST /admin/categories/id: update - {}", dto);
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("POST /admin/categories/id: delete");
        categoryService.delete(id);
    }
}
