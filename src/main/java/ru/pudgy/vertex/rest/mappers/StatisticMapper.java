package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Statistic;
import ru.pudgy.vertex.rest.dto.StatisticDto;

@Mapper(config = AppMapperConfig.class)
public interface StatisticMapper {

    @AfterMapping
    default void toDtoAfterMapping(Statistic statistic, @MappingTarget StatisticDto dto) {
        if(statistic.getValue() == null)
            dto.setValue(0);
    }

    @Mapping(target ="name", source = "statistic.name")
    @Mapping(target ="color", source = "statistic.color")
    @Mapping(target ="value", source = "statistic.value")
    StatisticDto toDto(Statistic statistic);
}
