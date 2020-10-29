package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Document;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository  extends CrudRepository<Document, UUID> {
    Page<Document> findBySchemataAndPurposeAndTextIlike(UUID schema, UUID purpose, String searchString, Pageable pageable);
    Page<Document> findBySchemataAndPurpose(UUID schema, UUID purpose, Pageable pageable);
    Page<Document> findBySchemataAndTextIlike(UUID schema, String searchString, Pageable pageable);
    Page<Document> findBySchemata(UUID schema, Pageable pageable);
    Optional<Document> findBySchemataAndId(UUID schema, UUID id);
    Number deleteBySchemataAndId(UUID schema, UUID id);
}
