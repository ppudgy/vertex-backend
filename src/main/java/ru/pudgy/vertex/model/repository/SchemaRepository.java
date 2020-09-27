package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Schemata;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchemaRepository extends CrudRepository<Schemata, UUID> {
    Optional<Schemata> findByName(String name);
}
