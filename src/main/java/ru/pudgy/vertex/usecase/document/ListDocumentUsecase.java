package ru.pudgy.vertex.usecase.document;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.DocumentRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListDocumentUsecase {
    private final TextService textService;
    private final DocumentRepository documentRepository;

    public Page<Document> execute(Schemata schemata,
                                  @Nullable Integer page,
                                  @Nullable Integer size,
                                  @Nullable UUID purpose,
                                  @Nullable  String searchString
    ) {
        Page<Document> result = Page.empty();
        Pageable pageable = Pageable.from(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10)
        );
        searchString = textService.formatSearchString(searchString);
        if(purpose != null && searchString != null) {
            result = documentRepository.findBySchemataAndPurposeAndTextIlike(schemata.getId(), purpose, searchString, pageable);
        } else if(purpose != null) {
            result = documentRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
        } else if(searchString != null) {
            result = documentRepository.findBySchemataAndTextIlike(schemata.getId(), searchString, pageable);
        } else {
            result = documentRepository.findBySchemata(schemata.getId(), pageable);
        }
        return result;
    }

}
