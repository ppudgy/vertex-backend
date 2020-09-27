package ru.pudgy.vertex.usecase.document;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.model.mappers.DocumentUpdater;
import ru.pudgy.vertex.model.repository.DocumentRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
@RequiredArgsConstructor
public class DocumentCreateUsecase {
    private final DocumentRepository documentRepository;
    private final DocumentUpdater documentUpdater;

    public Document execute(@NotNull Schemata schemata, @NotNull Note note) {
        return execute(schemata, documentUpdater.createDocument(note));
    }

    public Document execute(@NotNull Schemata schemata, @NotNull Todo todo) {
        return execute(schemata, documentUpdater.createDocument(todo));
    }

    public Document execute(@NotNull Schemata schemata, @NotNull Document udocument) {
        Document doc = documentUpdater.createDocument(schemata, udocument);
        return documentRepository.save(doc);
    }
}
