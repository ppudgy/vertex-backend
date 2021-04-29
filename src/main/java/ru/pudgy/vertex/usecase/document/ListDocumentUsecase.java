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
        Pageable pageable = Pageable.from(
                page != null ? page : 0,
                size != null ? size : 10
        );

        return textService.formatSearchString(searchString)
                .map(formatString -> {
                    if (purpose != null) {
                        return documentRepository.findBySchemataAndPurposeAndTextIlike(schemata.getId(), purpose, formatString, pageable);
                    } else {
                        return documentRepository.findBySchemataAndTextIlike(schemata.getId(), formatString, pageable);
                    }
                })
                .orElseGet(() -> {
                    if (purpose != null) {
                        return documentRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
                    } else {
                        return documentRepository.findBySchemata(schemata.getId(), pageable);
                    }
                });
    }

}
