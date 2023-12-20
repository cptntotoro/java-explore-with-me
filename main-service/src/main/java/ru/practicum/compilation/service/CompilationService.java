package ru.practicum.compilation.service;

import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

@Service
public interface CompilationService {
    Compilation add(NewCompilationDto compilationDto);

    void delete(Long compilationId);

    Compilation get(Long compId);

    List<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation update(Long compId, UpdateCompilationRequest compilationRequest);
}
