package ru.yandex.practicum.explore_with_me.compilation.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {

    Page<Compilation> findAllByPinned (Boolean pinned, Pageable pageable);
}
