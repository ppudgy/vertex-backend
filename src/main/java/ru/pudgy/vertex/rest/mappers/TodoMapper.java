package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
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

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "done", constant = "false")
    @Mapping(target = "endtime", expression = "java(null)")
    public abstract Todo toEntity(Schemata schema, TodoNewDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", source = "dto.origin")
    public abstract Todo toEntity(Schemata schema, TodoDto dto);
}
