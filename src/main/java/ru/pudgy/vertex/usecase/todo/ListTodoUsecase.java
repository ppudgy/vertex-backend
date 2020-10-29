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
        searchString = textService.formatSearchString(searchString);
        if(purpose != null && searchString != null) {
            result = todoRepository.findBySchemataAndPurposeAndDescriptionIlike(schemata.getId(), purpose, searchString, pageable);
        } else if(purpose != null) {
            result = todoRepository.findBySchemataAndPurpose(schemata.getId(), purpose, pageable);
        } else if(searchString != null) {
            result = todoRepository.findBySchemataAndDescriptionIlike(schemata.getId(), searchString, pageable);
        } else {
            result = todoRepository.findBySchemata(schemata.getId(), pageable);
        }
        return result;
    }
}
