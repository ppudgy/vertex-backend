package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Document;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.rest.dto.FragmentDto;
import ru.pudgy.vertex.rest.dto.FragmentNewDto;
import ru.pudgy.vertex.srvc.TextService;
import ru.pudgy.vertex.usecase.document.DocumentByIdUsecase;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)

public abstract class FragmentMapper {
    @Inject
    private TextService textUtil;
    @Inject
    private DocumentByIdUsecase documentByIdUsecase;

    @AfterMapping
    public void toDtoAfterMapping(Fragment fragment, @MappingTarget FragmentDto dto) {
        dto.setAnnotation(textUtil.createAnnotation(fragment.getText()));
        Document document = documentByIdUsecase.execute(fragment.getSchemata(), fragment.getDocument());
        dto.setDocname(document.getName());
        dto.setRoll(false);
    }

    @Mapping(target = "annotation", ignore = true)
    @Mapping(target = "start", source="fragment.posstart")
    @Mapping(target = "end", source="fragment.posend")
    @Mapping(target = "roll", ignore = true)
    @Mapping(target = "docname", ignore = true)
    public abstract  FragmentDto toDto(Fragment fragment);

     @AfterMapping
    public void toEntityAfterMapping(FragmentNewDto dto, @MappingTarget Fragment entity) {
        entity.setId(UUID.randomUUID());
        entity.setOrigin(ZonedDateTime.now());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "posstart", source = "dto.start")
    @Mapping(target = "posend", source = "dto.end")
    public abstract Fragment toEntity(FragmentNewDto dto);

    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "posstart", source = "dto.start")
    @Mapping(target = "posend", source = "dto.end")
    public abstract Fragment toEntity(FragmentDto dto);
}
