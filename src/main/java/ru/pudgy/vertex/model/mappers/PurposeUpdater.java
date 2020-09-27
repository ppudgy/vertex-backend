package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Purpose;

@Mapper(config = AppMapperConfig.class)
public interface PurposeUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    Purpose updateNote(Purpose from, @MappingTarget Purpose to);
}
