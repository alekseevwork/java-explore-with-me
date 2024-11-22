package ru.practicum.main_svc.category.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.category.CategoryService;
import ru.practicum.main_svc.category.dto.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("GET /categories: getAll from - {}, size - {}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable @Valid Long id) {
        log.info("GET /categories.id: getById - {}", id);
        return categoryService.getById(id);
    }
}
