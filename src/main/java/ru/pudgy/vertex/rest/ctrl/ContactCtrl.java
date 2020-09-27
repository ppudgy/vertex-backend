package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.rest.mappers.ContactMapper;
import ru.pudgy.vertex.rest.mappers.PersonMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.person.*;
import ru.pudgy.vertex.usecase.person.contact.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ContactCtrl {
    private final ListContactUsecase listContactUsecase;
    private final ContactByIdUsecase contactByIdUsecase;
    private final ContactCreateUsecase contactCreateUsecase;
    private final ContactUpdateUsecase contactUpdateUsecase;
    private final ContactDeleteUsecase contactDeleteUsecase;
    private final ContactMapper contactMapper;

    @Get(value = "/person/{person}/contact", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<ContactDto>> list(
            @PathVariable @NotNull UUID person,
            @Nullable UUID type,
            @Nullable String searchString
    ) {
        return SecurityHelper.currentSchema()
                .map(schema -> listContactUsecase.execute(schema, person, type, searchString))
                .map(_list -> _list.stream()
                        .map(contact -> contactMapper.toDto(contact))
                        .collect(Collectors.toList())

                )
                .map(_list -> HttpResponse.ok(_list))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }

    @Get(value = "/person/{person}/contact/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> topic(@PathVariable @NotNull UUID person, @PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> contactByIdUsecase.execute(schema, person, id))
                .map(contact -> contactMapper.toDto(contact))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/person/{person}/contact", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> create(@PathVariable @NotNull UUID person, @NotNull @Body ContactNewDto contactDto) {
        Contact newContact = contactMapper.toEntity(person, contactDto);
        return SecurityHelper.currentSchema()
                .map(schema -> contactCreateUsecase.execute(schema, person, newContact))
                .map(contact -> contactMapper.toDto(contact))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/person/{person}/contact/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> update(@PathVariable @NotNull UUID person, @NotNull @PathVariable UUID id, @NotNull @Body ContactDto contactDto) {
        Contact ucontact = contactMapper.toEntity(contactDto);
        return SecurityHelper.currentSchema()
                .map(schema -> contactUpdateUsecase.execute(schema, person, id, ucontact))
                .map(contact -> contactMapper.toDto(contact))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/person/{person}/contact/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID person, @PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> contactDeleteUsecase.execute(schema, person, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
