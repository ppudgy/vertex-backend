package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.*;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;

@Mapper(config = AppMapperConfig.class)
public interface TopicMapper {
    @Mapping(target ="id", source = "dto.id")
    @Mapping(target ="schemata", source = "schema.id")
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="checked", source = "dto.checked", defaultValue = "false")
    Topic toEntity(Schemata schema,TopicDto dto);

    @Mapping(target ="id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target ="schemata", source = "schema.id")
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="checked", source = "dto.checked", defaultValue = "false")
    Topic toEntity(Schemata schema, TopicNewDto dto);

    @Mapping(target ="id", source = "topic.id")
    @Mapping(target ="name", source = "topic.name")
    @Mapping(target ="checked", source = "topic.checked", defaultValue = "false")
    TopicDto toDto(Topic topic);
}
