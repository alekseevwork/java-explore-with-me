package ru.practicum.main_svc.compilation;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_svc.compilation.dto.CompilationDto;
import ru.practicum.main_svc.compilation.dto.NewCompilationDto;
import ru.practicum.main_svc.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.event.Event;
import ru.practicum.main_svc.event.EventRepository;
import ru.practicum.main_svc.event.EventService;

import java.util.List;

@Service
@Data
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService service;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size));
        return compilations.stream().map(compilation -> CompilationMapper.toDto(compilation, service)).toList();
    }

    @Override
    public CompilationDto getById(long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow();
        return CompilationMapper.toDto(compilation, service);
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newDto) {
        Compilation newCompilation = CompilationMapper.toCompilation(newDto);
        List<Event> events = eventRepository.findAllByIds(newDto.getEvents());
        newCompilation.setEvents(events);
        Compilation compilation = compilationRepository.save(newCompilation);
        return CompilationMapper.toDto(compilation, service);
    }

    @Transactional
    @Override
    public void delete(long id) {
        compilationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка событий с id /" + compId + "/ не найдена"));

        List<Event> events = eventRepository.findAllByIds(updateDto.getEvents());

        if (updateDto.getEvents() != null) {
            compilation.setEvents(events);
        }
        if (updateDto.getTitle() != null) {
            if (compilationRepository.findByTitle(updateDto.getTitle()).isPresent()) {
                throw new NotFoundException("Подборка событий с названием /" + updateDto.getTitle() +
                        "/ уже существует");
            }
            compilation.setTitle(updateDto.getTitle());
        }
        if (updateDto.getPinned() != null) {
            compilation.setPinned(updateDto.getPinned());
        }
        return CompilationMapper
                .toDto(compilationRepository.save(compilation), service);
    }
}
