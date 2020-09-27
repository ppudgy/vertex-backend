package ru.pudgy.vertex.usecase.todo;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.TodoRepository;
import ru.pudgy.vertex.usecase.document.DocumentCreateUsecase;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TodoDeleteUsecase {
    private final TodoRepository todoRepository;
    private final DocumentCreateUsecase documentCreateUsecase;

    public Number execute(Schemata schema, UUID id) {
        return todoRepository.findBySchemataAndId(schema.getId(), id)
                .map(note -> documentCreateUsecase.execute(schema, note))
                .map(document -> todoRepository.deleteBySchemataAndId(schema.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("todo(%s) not found", id.toString())));
    }
}
