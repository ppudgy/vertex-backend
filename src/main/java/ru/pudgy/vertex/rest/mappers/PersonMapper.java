package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.rest.dto.PersonDto;
import ru.pudgy.vertex.rest.dto.PersonNewDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface PersonMapper {

    @Mapping(target ="id", source = "dto.id")
    @Mapping(target ="schemata", source = "schema.id")
    @Mapping(target ="checked", defaultValue = "false")
    @Mapping(target ="origin", source = "dto.origin")
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="sex", expression = "java(\"women\".equalsIgnoreCase(dto.getSex())? \"women\": \"men\")")
    Person toEntity(Schemata schema, PersonDto dto);

    @Mapping(target ="id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target ="schemata", source = "schema.id")
    @Mapping(target ="origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="checked",  constant = "false")
    Person toEntity(Schemata schema, PersonNewDto dto);

    @Mapping(target ="checked",  defaultValue = "false")
    PersonDto toDto(Person person);
}
