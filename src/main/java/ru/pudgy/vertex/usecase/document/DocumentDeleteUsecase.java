package ru.pudgy.vertex.usecase.document;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.DocumentRepository;
import ru.pudgy.vertex.model.repository.NoteRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class DocumentDeleteUsecase {
    private final DocumentRepository documentRepository;

    public Number execute(@NotNull Schemata schemata, @NotNull UUID id) {
        return documentRepository.findBySchemataAndId(schemata.getId(), id)
                .map(document -> documentRepository.deleteBySchemataAndId(schemata.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("document(%s) not found", id.toString())));
    }
}
