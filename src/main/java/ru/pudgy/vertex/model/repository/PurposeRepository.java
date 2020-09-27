package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Purpose;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurposeRepository extends CrudRepository<Purpose, UUID> {
    List<Purpose> findBySchemata(UUID schema);
    Optional<Purpose> findBySchemataAndId(UUID schema, UUID id);
    Number deleteBySchemataAndId(UUID schema, UUID id);
}
