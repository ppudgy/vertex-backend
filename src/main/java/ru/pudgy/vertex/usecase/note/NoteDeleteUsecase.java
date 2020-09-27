package ru.pudgy.vertex.usecase.note;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.NoteRepository;
import ru.pudgy.vertex.usecase.document.DocumentCreateUsecase;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class NoteDeleteUsecase {
    private final NoteRepository noteRepository;
    private final DocumentCreateUsecase documentCreateUsecase;

    public Number execute(@NotNull Schemata schema, @NotNull UUID id) {
        return noteRepository.findBySchemataAndId(schema.getId(), id)
                .map(note -> documentCreateUsecase.execute(schema, note))
                .map(document -> noteRepository.deleteBySchemataAndId(schema.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("note(%s) not found", id.toString())));
    }
}
