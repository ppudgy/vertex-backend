package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Todo;

@Mapper(config = AppMapperConfig.class)
public interface TodoUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    Todo updateTodo(Todo from, @MappingTarget Todo to);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    Todo createTodo(Schemata schema, Todo from);
}
