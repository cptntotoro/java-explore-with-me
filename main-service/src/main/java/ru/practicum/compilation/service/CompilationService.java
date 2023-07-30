package ru.practicum.compilation.service;

import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@Service
public interface CompilationService {
    CompilationDto add(NewCompilationDto compilationDto);

    void delete(Long compilationId);

    CompilationDto get(Long compId);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto update(Long compId, UpdateCompilationRequest compilationRequest);
}
