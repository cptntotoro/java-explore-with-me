package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addEventCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {

        log.info("Calling POST: /admin/compilations with 'compilationDto': {}", compilationDto);
        return compilationMapper.compilationToCompilationDto(compilationService.add(compilationDto));
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateEventCompilation(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest compRequest) {

        log.info("Calling PATCH: /admin/compilations/{compId} with 'compId': {}", compId);
        return compilationMapper.compilationToCompilationDto(compilationService.update(compId, compRequest));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventCompilation(@PathVariable Long compId) {

        log.info("Calling DELETE: /admin/compilations/{compId} with 'compId': {}", compId);
        compilationService.delete(compId);
    }
}