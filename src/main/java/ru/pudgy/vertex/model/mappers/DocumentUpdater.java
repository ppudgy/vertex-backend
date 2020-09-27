package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.*;

@Mapper(config = AppMapperConfig.class)
public interface DocumentUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    Document updateDocument(Document from, @MappingTarget Document to);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "from.schemata")
    @Mapping(target = "name", expression = "java(\"from note:\" + from.getOrigin().format(java.time.format.DateTimeFormatter.ISO_INSTANT))")
    @Mapping(target = "date", source = "from.origin")
    @Mapping(target = "location", constant = "note")
    @Mapping(target = "text", source = "from.text")
    @Mapping(target = "purpose", source = "from.purpose")
    @Mapping(target = "mime", constant = "text/plain")
    @Mapping(target = "treated", constant = "false")
    Document createDocument(Note from);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "from.schemata")
    @Mapping(target = "name", expression = "java(\"from note:\" + from.getOrigin().format(java.time.format.DateTimeFormatter.ISO_INSTANT))")
    @Mapping(target = "date", source = "from.origin")
    @Mapping(target = "location", constant = "todo")
    @Mapping(target = "text", source = "from.description")
    @Mapping(target = "purpose", source = "from.purpose")
    @Mapping(target = "mime", constant = "text/plain")
    @Mapping(target = "treated", constant = "false")
    Document createDocument(Todo from);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "name", source = "from.name")
    @Mapping(target = "date", source = "from.date")
    @Mapping(target = "location", source = "from.location")
    @Mapping(target = "text", source = "from.text")
    @Mapping(target = "purpose", source = "from.purpose")
    @Mapping(target = "mime", source = "from.mime")
    @Mapping(target = "treated", constant = "false")
    Document createDocument(Schemata schema, Document from);
}
