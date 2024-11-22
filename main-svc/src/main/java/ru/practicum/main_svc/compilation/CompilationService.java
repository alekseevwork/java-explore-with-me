package ru.practicum.main_svc.compilation;

import ru.practicum.main_svc.compilation.dto.CompilationDto;
import ru.practicum.main_svc.compilation.dto.NewCompilationDto;
import ru.practicum.main_svc.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(long id);

    CompilationDto create(NewCompilationDto newDto);

    void delete(long id);

    CompilationDto update(Long compId, UpdateCompilationRequest updateDto);
}
