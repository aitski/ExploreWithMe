package ru.yandex.practicum.explore_with_me.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
