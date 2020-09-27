package ru.pudgy.vertex.usecase.note;


import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.NoteRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.annotation.Nullable;
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
        Page<Note> result = Page.empty();
        Pageable pageable = Pageable.from(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10)
        );
        searchString = textService.formatSearchString(searchString);
        if(purpose != null && searchString != null) {
            result = noteRepository.findBySchemataAndPurposeAndTextLike(schemata.getId(), purpose, searchString, pageable);
        } else if(purpose != null) {
            result = noteRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
        } else if(searchString != null) {
            result = noteRepository.findBySchemataAndTextLike(schemata.getId(), searchString, pageable);
        } else {
            result = noteRepository.findBySchemata(schemata.getId(), pageable);
        }
        return result;
    }
}
