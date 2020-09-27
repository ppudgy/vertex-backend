package ru.pudgy.vertex.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;

import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public interface FragmentUpdater {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    Fragment updateFragment(Fragment from, @MappingTarget Fragment to);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "name", source = "from.name")
    @Mapping(target = "document", source = "doc")
    Fragment createFragment(Schemata schema, UUID doc, Fragment from);
}
