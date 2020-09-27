package ru.pudgy.vertex.model.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;

@Mapper(config = AppMapperConfig.class)
public interface PersonUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "origin", ignore = true)
    Person updatePerson(Person from, @MappingTarget Person to);

    @AfterMapping
    default void createPersonAfterMapping(Schemata schema, Person from, @MappingTarget Person created) {
        if(from.getChecked() == null)
            created.setChecked(false);
    }

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "name", source = "from.name")
    @Mapping(target = "sername", source = "from.sername")
    @Mapping(target = "family", source = "from.family")
    @Mapping(target = "birthday", source = "from.birthday")
    @Mapping(target = "sex", source = "from.sex")
    @Mapping(target = "checked", source = "from.checked")
    Person createPerson(Schemata schema, Person from);
}