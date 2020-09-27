package ru.pudgy.vertex.usecase.todo;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.model.mappers.TodoUpdater;
import ru.pudgy.vertex.model.repository.TodoRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TodoUpdateUsecase {
    private final TodoRepository todoRepository;
    private final TodoUpdater updater;

    public Todo execute(Schemata schema, UUID id, Todo utodo) {
        return todoRepository.findBySchemataAndId(schema.getId(), id)
                .map(todo -> updater.updateTodo(utodo, todo))
                .map(todo -> todoRepository.update(todo))
                .orElseThrow(() -> new NotFoundException(String.format("todo(%s) not found", id.toString())));
    }
}
