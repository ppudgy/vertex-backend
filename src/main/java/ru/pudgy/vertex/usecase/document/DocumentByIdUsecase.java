package ru.pudgy.vertex.usecase.document;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.DocumentRepository;
import ru.pudgy.vertex.model.repository.NoteRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class DocumentByIdUsecase {
    private final DocumentRepository documentRepository;

    public Document execute(@NotNull Schemata schemata, @NotNull UUID id) {
        return execute(schemata.getId(), id);
    }

    public Document execute(@NotNull UUID schema, @NotNull UUID id) {
        return documentRepository.findBySchemataAndId(schema, id)
                .orElseThrow(() -> new NotFoundException(String.format("document(%s) not found", id.toString())));
    }
}
