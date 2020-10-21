package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Note;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.rest.dto.NoteDto;
import ru.pudgy.vertex.rest.dto.NoteNewDto;
import ru.pudgy.vertex.srvc.TextService;
import ru.pudgy.vertex.usecase.purpose.PurposeByIdUsecase;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)

public abstract class NoteMapper {
    @Inject
    private TextService textUtil;
    @Inject
    private PurposeByIdUsecase purposeByIdUsecase;

    @AfterMapping
    public void toDtoAfterMapping(Note note, @MappingTarget NoteDto dto) {
        dto.setAnnotation(textUtil.createAnnotation(note.getText()));
        Purpose purpose = purposeByIdUsecase.execute(note.getSchemata(), note.getPurpose());
        dto.setPurposeName(purpose.getName());
        dto.setPurposeColor(purpose.getColor());
    }

    @Mapping(target ="id", source = "note.id")
    @Mapping(target ="origin", source = "note.origin")
    @Mapping(target ="text", source = "note.text")
    @Mapping(target ="purpose", source = "note.purpose")
    @Mapping(target = "annotation", ignore = true)
    @Mapping(target = "purposeName", ignore = true)
    @Mapping(target = "purposeColor", ignore = true)
    public abstract  NoteDto toDto(Note note);

    @Mapping(target ="text", source = "dto.text")
    @Mapping(target ="purpose", source = "dto.purpose")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    public abstract Note toEntity(Schemata schema, NoteNewDto dto);

    @Mapping(target ="text", source = "dto.text")
    @Mapping(target ="purpose", source = "dto.purpose")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", source = "dto.origin")
    public abstract Note toEntity(Schemata schema, NoteDto dto);


}
