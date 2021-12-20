package ru.pudgy.vertex.usecase.note;


import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.NoteRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListNoteUsecase {
    private final TextService textService;
    private final NoteRepository noteRepository;

    public Page<Note> execute(Schemata schemata,
                              @Nullable Integer page,
                              @Nullable Integer size,
                              @Nullable UUID purpose,
                              @Nullable  String searchString
    ) {
        Pageable pageable = Pageable.from(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10)
        );
        return textService.formatSearchString(searchString)
                .map(formatString -> {
                    if(purpose != null) {
                        return noteRepository.findBySchemataAndPurposeAndTextIlike(schemata.getId(), purpose, formatString, pageable);
                    } else{
                        return noteRepository.findBySchemataAndTextIlike(schemata.getId(), formatString, pageable);
                    }
                })
                .orElseGet(() -> {
                    if(purpose != null) {
                        return noteRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
                    } else {
                        return noteRepository.findBySchemata(schemata.getId(), pageable);
                    }
                });
    }
}
