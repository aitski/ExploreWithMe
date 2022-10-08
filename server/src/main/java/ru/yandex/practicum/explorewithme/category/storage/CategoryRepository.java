package ru.yandex.practicum.explorewithme.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explorewithme.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
