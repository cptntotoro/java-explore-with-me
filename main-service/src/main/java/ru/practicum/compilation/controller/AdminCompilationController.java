package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addEventCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {

        log.info("Calling POST: /admin/compilations with 'compilationDto': {}", compilationDto);
        return compilationService.add(compilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateEventCompilation(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest compRequest) {

        log.info("Calling PATCH: /admin/compilations/{compId} with 'compId': {}", compId);
        return compilationService.update(compId, compRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventCompilation(@PathVariable Long compId) {

        log.info("Calling DELETE: /admin/compilations/{compId} with 'compId': {}", compId);
        compilationService.delete(compId);
    }
}