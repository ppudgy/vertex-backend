package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.rest.dto.PersonDto;
import ru.pudgy.vertex.rest.dto.PersonNewDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface PersonMapper {

    @AfterMapping
    default void toEntityAfterMapping(PersonDto dto, @MappingTarget Person person) {
        if(dto.getChecked() == null)
            person.setChecked(false);
        person.setSex(
            "women".equalsIgnoreCase(dto.getSex())
                ? "women"
                : "men"
        );
    }

    @Mapping(target ="schemata", ignore = true)
    Person toEntity(PersonDto dto);

    @AfterMapping
    default void toEntityAfterMapping(PersonNewDto dto, @MappingTarget Person person) {
        person.setId(UUID.randomUUID());
        person.setOrigin(ZonedDateTime.now());
        person.setChecked(false);
    }

    @Mapping(target ="id", ignore = true)
    @Mapping(target ="schemata", ignore = true)
    @Mapping(target ="origin",  ignore = true)
    @Mapping(target ="checked",  ignore = true)
    Person toEntity(PersonNewDto dto);


    @AfterMapping
    default void toDtoAfterMapping(Person person, @MappingTarget PersonDto dto) {
        if(person.getChecked() == null)
            dto.setChecked(false);
    }

    PersonDto toDto(Person person);
}
