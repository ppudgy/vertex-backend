package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Todo;
import ru.pudgy.vertex.rest.dto.TodoDto;
import ru.pudgy.vertex.rest.dto.TodoNewDto;
import ru.pudgy.vertex.usecase.purpose.PurposeByIdUsecase;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public abstract class TodoMapper {
    @Inject
    private PurposeByIdUsecase purposeByIdUsecase;

    @AfterMapping
    public void toDtoAfterMapping(Todo note, @MappingTarget TodoDto dto) {
        Purpose purpose = purposeByIdUsecase.execute(note.getSchemata(), note.getPurpose());
        dto.setPurposeName(purpose.getName());
        dto.setPurposeColor(purpose.getColor());
    }

    @Mapping(target = "purposeName", ignore = true)
    @Mapping(target = "purposeColor", ignore = true)
    public abstract TodoDto toDto(Todo note);

    @AfterMapping
    public void toEntityAfterMapping(TodoNewDto dto, @MappingTarget Todo entity) {
        entity.setId(UUID.randomUUID());
        entity.setSchemata(null);
        entity.setOrigin(ZonedDateTime.now());
        entity.setDone(false);
        entity.setEndtime(null);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "done", ignore = true)
    @Mapping(target = "endtime", ignore = true)
    public abstract Todo toEntity(TodoNewDto dto);

    @Mapping(target = "schemata", ignore = true)
    public abstract Todo toEntity(TodoDto dto);
}
