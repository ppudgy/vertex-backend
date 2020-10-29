package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import ru.pudgy.vertex.model.entity.Note;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends PageableRepository<Note, UUID> {
    Page<Note> findBySchemataAndPurpose(UUID schemata, UUID purpose, Pageable pageable);
    Page<Note> findBySchemataAndTextIlike(UUID schemata, String searchString, Pageable pageable);
    Page<Note> findBySchemataAndPurposeAndTextIlike(UUID schemata, UUID purpose, String searchString, Pageable pageable);
    Page<Note> findBySchemata(UUID schemata, Pageable pageable);
    Optional<Note> findBySchemataAndId(UUID schemata, UUID id);

    Number deleteBySchemataAndId(UUID schemata, UUID id);
}
