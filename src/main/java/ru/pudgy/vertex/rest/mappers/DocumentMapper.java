package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.rest.dto.DocumentDto;
import ru.pudgy.vertex.srvc.TextService;
import ru.pudgy.vertex.usecase.purpose.PurposeByIdUsecase;

import javax.inject.Inject;

@Mapper(config = AppMapperConfig.class)

public abstract class DocumentMapper {
    @Inject
    private TextService textUtil;
    @Inject
    private PurposeByIdUsecase purposeByIdUsecase;

    @AfterMapping
    public void toDtoAfterMapping(Document document, @MappingTarget DocumentDto dto) {
        dto.setAnnotation(textUtil.createAnnotation(document.getText()));
        Purpose purpose = purposeByIdUsecase.execute(document.getSchemata(), document.getPurpose());
        dto.setPurposeName(purpose.getName());
        dto.setPurposeColor(purpose.getColor());
    }

    @Mapping(target ="id", source = "document.id")
    @Mapping(target ="origin", source = "document.origin")
    @Mapping(target ="text", source = "document.text")
    @Mapping(target ="purpose", source = "document.purpose")
    @Mapping(target = "annotation", ignore = true)
    @Mapping(target = "purposeName", ignore = true)
    @Mapping(target = "purposeColor", ignore = true)
    @Mapping(target = "roll", constant = "false")
    public abstract  DocumentDto toDto(Document document);

    @Mapping(target ="text", source = "dto.text")
    @Mapping(target ="purpose", source = "dto.purpose")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", source = "dto.origin")
    @Mapping(target = "name", source = "dto.name")
    public abstract Document toEntity(Schemata schema, DocumentDto dto);


}
