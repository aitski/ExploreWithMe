package ru.yandex.practicum.explorewithme.category.service;

import ru.yandex.practicum.explorewithme.category.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();
    Category create(Category user);
    void delete (long id);
    Category getById(long id);

}
