package ru.practicum.main_svc.compilation;

import ru.practicum.main_svc.compilation.dto.CompilationDto;
import ru.practicum.main_svc.compilation.dto.NewCompilationDto;
import ru.practicum.main_svc.event.EventMapper;
import ru.practicum.main_svc.event.EventService;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto dto) {
        if (dto == null) {
            return null;
        }
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());
        return compilation;
    }

    public static CompilationDto toDto(Compilation compilation, EventService service) {
        if (compilation == null) {
            return null;
        }
        System.out.println("Compilation to Dto");
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents().stream().map(event -> EventMapper.toShortDto(event, service)).toList(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
