package ru.practicum.main_svc.category;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.category.dto.CategoryDto;
import ru.practicum.main_svc.category.dto.NewCategoryDto;
import ru.practicum.main_svc.error.exception.CategoryConflictException;
import ru.practicum.main_svc.error.exception.DuplicationNameException;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.event.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicationNameException("Name - " + dto.getName() + " already exist");
        }
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.toCategory(dto)));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        if (eventRepository.findFirstByCategoryId(id).isPresent()) {
            throw new CategoryConflictException("Category by id - " + id + " used in events");
        }
        categoryRepository.deleteById(id);
    }


    @Override
    public CategoryDto update(Long categoryId, CategoryDto dto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        if (categoryRepository.findByName(dto.getName()).isPresent() && !category.getName().equals(dto.getName())) {
            throw new CategoryConflictException("Category by name - " + dto.getName() + " already exist");
        }
        category.setName(dto.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).stream().map(CategoryMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getById(Long id) {
        return CategoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found")));
    }
}
