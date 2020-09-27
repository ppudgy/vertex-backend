package ru.pudgy.vertex.usecase.todo;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.model.repository.TodoRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TodoByIdUsecase {
    private final TodoRepository todoRepository;

    public Todo execute(Schemata schema, UUID id) {
        return todoRepository.findBySchemataAndId(schema.getId(), id)
                .orElseThrow(() -> new NotFoundException(String.format("todo(%s) not found", id.toString())));

    }
}
