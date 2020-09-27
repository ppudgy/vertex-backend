package ru.pudgy.vertex.usecase.document;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.DocumentUpdater;
import ru.pudgy.vertex.model.repository.DocumentRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class DocumentUpdateUsecase {
    private final DocumentRepository documentRepository;
    private final DocumentUpdater updater;

    public Document execute(@NotNull Schemata schemata, @NotNull UUID id, @NotNull Document udocument) {
        return documentRepository.findBySchemataAndId(schemata.getId(), id)
                .map(document -> updater.updateDocument(udocument, document))
                .map(document -> documentRepository.update(document))
                .orElseThrow(() -> new NotFoundException(String.format("document(%s) not found", id.toString())));
    }
}
