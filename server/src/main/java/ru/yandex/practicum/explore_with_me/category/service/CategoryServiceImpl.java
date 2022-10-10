package ru.yandex.practicum.explore_with_me.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.category.model.Category;
import ru.yandex.practicum.explore_with_me.category.storage.CategoryRepository;
import ru.yandex.practicum.explore_with_me.exception.exceptions.NotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        List<Category> list = categoryRepository.findAll();
        log.debug("list of categories returned: {}", list);
        return list;
    }

    @Override
    public Category create(Category category) {

        Category categoryNew = categoryRepository.save(category);
        log.debug("Category created/updated: {}", categoryNew);
        return categoryNew;
    }

    @Override
    public void delete(long id) {
        categoryRepository.delete(getById(id));
        log.debug("category with id {} deleted", id);
    }

    @Override
    public Category getById(long id) {

        Category category = categoryRepository.findById(id).orElseThrow
                (() -> new NotFoundException("Category", id));
        log.debug("Category returned {}", category);
        return category;
    }
}
