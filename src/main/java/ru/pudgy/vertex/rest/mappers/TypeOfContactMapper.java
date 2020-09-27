package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;
import ru.pudgy.vertex.rest.dto.TypeOfContactDto;

@Mapper(config = AppMapperConfig.class)
public interface TypeOfContactMapper {

    @Mapping(target ="id", source = "dto.id")
    @Mapping(target ="name", source = "dto.name")
    TypeOfContact toEntity(TypeOfContactDto dto);

    @Mapping(target ="id", source = "entity.id")
    @Mapping(target ="name", source = "entity.name")
    TypeOfContactDto toDto(TypeOfContact entity);
}
