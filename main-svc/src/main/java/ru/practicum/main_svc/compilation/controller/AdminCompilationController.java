package ru.practicum.main_svc.compilation.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_svc.compilation.CompilationService;
import ru.practicum.main_svc.compilation.dto.CompilationDto;
import ru.practicum.main_svc.compilation.dto.NewCompilationDto;
import ru.practicum.main_svc.compilation.dto.UpdateCompilationRequest;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newDto) {
        log.info("POST /admin/compilations: create - {}, ", newDto);
        return compilationService.create(newDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("DELETE /admin/compilations/id: delete by id - {}", id);
        compilationService.delete(id);
    }

    @PatchMapping("/{id}")
    public CompilationDto update(@PathVariable long id,
                                 @RequestBody @Valid UpdateCompilationRequest updateRequest) {
        log.info("PATCH /admin/compilations/id: update by id - {}, dto - {}", id, updateRequest);
        return compilationService.update(id, updateRequest);
    }

}
