package ru.pudgy.vertex.usecase.note;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.NoteRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class NoteByIdUsecase {
    private final NoteRepository noteRepository;

    public Note execute(@NotNull Schemata schemata, @NotNull UUID id) {
        return noteRepository.findBySchemataAndId(schemata.getId(), id)
                .orElseThrow(() -> new NotFoundException(String.format("note(%s) not found", id.toString())));
    }
}
