package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;

@Mapper(config = AppMapperConfig.class)
public interface ContactUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "person", ignore = true)
    Contact updateContact(Contact from, @MappingTarget Contact to);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    Contact createContact(Schemata schema, Contact from);
}