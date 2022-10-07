package ru.yandex.practicum.ExploreWithMe.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ExploreWithMe.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
