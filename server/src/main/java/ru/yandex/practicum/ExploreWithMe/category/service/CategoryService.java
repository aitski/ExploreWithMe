package ru.yandex.practicum.ExploreWithMe.category.service;

import ru.yandex.practicum.ExploreWithMe.category.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();
    Category create(Category user);
    void delete (long id);
    Category getById(long id);

}
