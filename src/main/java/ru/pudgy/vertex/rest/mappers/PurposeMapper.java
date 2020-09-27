package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.rest.dto.PurposeDto;
import ru.pudgy.vertex.rest.dto.PurposeNewDto;

import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface PurposeMapper {
    @Mapping(target ="id", source = "purpose.id")
    @Mapping(target ="name", source = "purpose.name")
    @Mapping(target ="color", source = "purpose.color")
    @Mapping(target ="active", source = "purpose.active")
    PurposeDto toDto(Purpose purpose);

    @AfterMapping
    default void toEntityAfterMapping(PurposeNewDto dto, @MappingTarget Purpose entity) {
        entity.setId(UUID.randomUUID());
        entity.setActive(true);
    }

    @Mapping(target ="id", ignore = true)
    @Mapping(target ="schemata", ignore = true)
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="color", source = "dto.color")
    @Mapping(target ="active", ignore = true)
    Purpose toEntity(PurposeNewDto dto);

    @Mapping(target ="id", source = "dto.id")
    @Mapping(target ="schemata", ignore = true)
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="color", source = "dto.color")
    @Mapping(target ="active", source = "dto.active")
    Purpose toEntity(PurposeDto dto);
}
