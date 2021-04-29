package ru.pudgy.vertex.usecase.todo;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.model.repository.TodoRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListTodoUsecase {
    private final TextService textService;
    private final TodoRepository todoRepository;

    public Page<Todo> execute(Schemata schemata,
                              Integer page,
                              Integer size,
                              UUID purpose,
                              String searchString) {
        Page<Todo> result = Page.empty();
        Pageable pageable = Pageable.from(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10)
        );
        return textService.formatSearchString(searchString)
                .map(formatString -> {
                    if(purpose != null) {
                        return todoRepository.findBySchemataAndPurposeAndDescriptionIlike(schemata.getId(), purpose, formatString, pageable);
                    } else {
                        return todoRepository.findBySchemataAndDescriptionIlike(schemata.getId(), formatString, pageable);
                    }
                })
                .orElseGet(() -> {
                    if(purpose != null) {
                        return todoRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
                    } else {
                        return todoRepository.findBySchemata(schemata.getId(), pageable);
                    }
                });
    }
}
