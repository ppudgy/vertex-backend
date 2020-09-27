package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.TypeOfContact;

import java.util.UUID;

@Repository
public interface TypeOfContactRepository extends CrudRepository<TypeOfContact, UUID> {
}
