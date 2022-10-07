package ru.yandex.practicum.ExploreWithMe.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ExploreWithMe.category.model.Category;
import ru.yandex.practicum.ExploreWithMe.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/{catId}")
    public Category getByd(@PathVariable long catId) {
        return categoryService.getById(catId);
    }

    @GetMapping("/categories")
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @PostMapping("/admin/categories")
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PatchMapping("/admin/categories")
    public Category patch(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void delete(@PathVariable long catId) {
        categoryService.delete(catId);
    }

}
