package ru.pudgy.vertex.usecase.note;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.NoteUpdater;
import ru.pudgy.vertex.model.repository.NoteRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class NoteUpdateUsecase {
    private final NoteRepository noteRepository;
    private final NoteUpdater updater;

    public Note execute(@NotNull Schemata schemata, @NotNull UUID id, @NotNull Note unote) {
        return noteRepository.findBySchemataAndId(schemata.getId(), id)
                .map(note -> updater.updateNote(unote, note))
                .map(note -> noteRepository.update(note))
                .orElseThrow(() -> new NotFoundException(String.format("note(%s) not found", id.toString())));
    }
}
