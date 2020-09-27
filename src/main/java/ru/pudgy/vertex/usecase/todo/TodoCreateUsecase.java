package ru.pudgy.vertex.usecase.todo;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.model.mappers.TodoUpdater;
import ru.pudgy.vertex.model.repository.TodoRepository;

import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor
public class TodoCreateUsecase {
    private final TodoRepository todoRepository;
    private final TodoUpdater todoUpdater;

    public Todo execute(Schemata schema, Todo todo) {
        return todoRepository.save(todoUpdater.createTodo(schema, todo));
    }
}
