package ru.yandex.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explorewithme.compilation.model.CompilationDto;
import ru.yandex.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.yandex.practicum.explorewithme.compilation.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping("/admin/compilations")
    public CompilationDto create(@RequestBody NewCompilationDto compilationDto) {
        return compilationMapper.convertToDto(
                compilationService.create(
                        compilationMapper.convertFromDto(compilationDto)));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable long compId) {
        compilationService.delete(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId, @PathVariable long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pin(@PathVariable long compId) {
        compilationService.pin(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpin(@PathVariable long compId) {
        compilationService.unpin(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAll(pinned, from, size).stream()
                .map(compilationMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        return compilationMapper.convertToDto(compilationService.getById(compId));
    }

}
