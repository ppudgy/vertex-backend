package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Schemata;

@Mapper(config = AppMapperConfig.class)
public interface NoteUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    Note updateNote(Note from, @MappingTarget Note to);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    Note createNote(Schemata schema, Note from);
}
