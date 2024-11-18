package ru.practicum.main_svc.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.main_svc.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}