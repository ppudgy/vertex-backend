package ru.pudgy.vertex.usecase.note;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.NoteUpdater;
import ru.pudgy.vertex.model.repository.NoteRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
@RequiredArgsConstructor
public class NoteCreateUsecase {
    private final NoteRepository noteRepository;
    private final NoteUpdater updater;

    public Note execute(@NotNull Schemata schemata, @NotNull Note note) {
        return noteRepository.save(updater.createNote(schemata, note));
    }
}
