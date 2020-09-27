package ru.pudgy.vertex.rest.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.pudgy.vertex.cfg.AppMapperConfig;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.rest.dto.ContactDto;
import ru.pudgy.vertex.rest.dto.ContactNewDto;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactByIdUsecase;

import javax.inject.Inject;
import java.time.ZonedDateTime;
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

    @AfterMapping
    public void toEntityAfterMapping(UUID person, ContactNewDto dto, @MappingTarget Contact entity) {
        entity.setOrigin(ZonedDateTime.now());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "person", source = "person")
    public abstract Contact toEntity(UUID person, ContactNewDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schemata", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "typeofcontact",source = "type")
    public abstract Contact toEntity(ContactDto dto);
}
