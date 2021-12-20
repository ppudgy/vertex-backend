package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.rest.dto.ContactDto;
import ru.pudgy.vertex.rest.dto.ContactNewDto;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactByIdUsecase;

import javax.inject.Inject;
import java.util.UUID;

@Mapper(config = AppMapperConfig.class)
public abstract class ContactMapper {
    @Inject
    private TypeOfContactByIdUsecase typeOfContactByIdUsecase;

    @AfterMapping
    public void toDtoAfterMapping(Contact contact, @MappingTarget ContactDto dto) {
        TypeOfContact typeOfContact = typeOfContactByIdUsecase.execute(contact.getSchemata(), contact.getTypeofcontact());
        dto.setTypename(typeOfContact.getName());
    }

    @Mapping(target = "type", source = "typeofcontact")
    @Mapping(target = "typename", ignore = true)
    public abstract ContactDto toDto(Contact contact);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", expression = "java(java.time.ZonedDateTime.now())")
    @Mapping(target = "person", source = "person")
    public abstract Contact toEntity(Schemata schema, UUID person, ContactNewDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "schemata", source = "schema.id")
    @Mapping(target = "origin", source = "dto.origin")
    @Mapping(target = "person", source = "person")
    @Mapping(target = "typeofcontact",source = "dto.type")
    public abstract Contact toEntity(Schemata schema, UUID person, ContactDto dto);
}
