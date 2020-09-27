package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Todo;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends CrudRepository<Todo, UUID> {
    Page<Todo> findBySchemataAndPurposeAndDescriptionLike(UUID schema, UUID purpose, String searchString, Pageable pageable);
    Page<Todo> findBySchemataAndPurpose(UUID schema, UUID purpose, Pageable pageable);
    Page<Todo> findBySchemataAndDescriptionLike(UUID schema, String searchString, Pageable pageable);
    Page<Todo> findBySchemata(UUID schema, Pageable pageable);
    Optional<Todo> findBySchemataAndId(UUID schema, UUID id);
    Number deleteBySchemataAndId(UUID schema, UUID id);
}
