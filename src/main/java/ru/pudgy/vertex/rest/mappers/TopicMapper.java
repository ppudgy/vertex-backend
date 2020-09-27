package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;

@Mapper(config = AppMapperConfig.class)
public interface TopicMapper {

    @AfterMapping
    default void toEntityAfterMapping(TopicDto dto, @MappingTarget Topic topic) {
        if(dto.getChecked() == null)
            topic.setChecked(false);
    }

    @Mapping(target ="id", source = "dto.id")
    @Mapping(target ="schemata", ignore = true)
    @Mapping(target ="name", source = "dto.name")
    Topic toEntity(TopicDto dto);

    @AfterMapping
    default void toEntityAfterMapping(TopicNewDto dto, @MappingTarget Topic topic) {
        if(dto.getChecked() == null)
            topic.setChecked(false);
    }

    @Mapping(target ="id", ignore = true)
    @Mapping(target ="schemata", ignore = true)
    @Mapping(target ="name", source = "dto.name")
    @Mapping(target ="checked", source = "dto.checked")
    Topic toEntity(TopicNewDto dto);

    @AfterMapping
    default void toDtoAfterMapping(Topic topic, @MappingTarget TopicDto dto) {
        if(topic.getChecked() == null)
            dto.setChecked(false);
    }

    @Mapping(target ="id", source = "topic.id")
    @Mapping(target ="name", source = "topic.name")
    @Mapping(target ="checked", source = "topic.checked")
    TopicDto toDto(Topic topic);
}
