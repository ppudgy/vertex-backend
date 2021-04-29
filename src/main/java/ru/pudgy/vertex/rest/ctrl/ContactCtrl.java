package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.result.Result;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.rest.mappers.ContactMapper;
import ru.pudgy.vertex.rest.mappers.ErrorMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.person.contact.*;

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
        return SecurityHelper.currentSchemata()
                .map(schema -> listContactUsecase.execute(schema, person, type, searchString))
                .map(_list -> Result.ok(_list.stream().map(contactMapper::toDto).collect(Collectors.toList())))
                .map(_list -> Result.ok(HttpResponse.ok(_list)))
                .recover(ErrorMapper::toHttpResponse);
    }

    @Get(value = "/person/{person}/contact/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> topic(@PathVariable @NotNull UUID person, @PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchemata()
                .map(schema -> contactByIdUsecase.execute(schema, person, id))
                .map(contact -> Result.ok(contactMapper.toDto(contact)))
                .map(dto -> Result.ok(HttpResponse.ok(dto)))
                .recover(ErrorMapper::toHttpResponse);
    }

    @Put(value = "/person/{person}/contact", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> create(@PathVariable @NotNull UUID person, @NotNull @Body ContactNewDto contactDto) {
        return SecurityHelper.currentSchemata()
                .map(schema -> contactCreateUsecase.execute(schema, person, contactMapper.toEntity(schema, person, contactDto)))
                .map(contact -> Result.ok(contactMapper.toDto(contact)))
                .map(dto -> Result.ok(HttpResponse.created(dto)))
                .recover(ErrorMapper::toHttpResponse);
    }

    @Post(value = "/person/{person}/contact/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<ContactDto> update(@PathVariable @NotNull UUID person, @NotNull @PathVariable UUID id, @NotNull @Body ContactDto contactDto) {
        return SecurityHelper.currentSchemata()
                .map(schema -> contactUpdateUsecase.execute(schema, person, id, contactMapper.toEntity(schema, person, contactDto)))
                .map(contact -> Result.ok(contactMapper.toDto(contact)))
                .map(dto -> Result.ok(HttpResponse.ok().body(dto)))
                .recover(ErrorMapper::toHttpResponse);
    }

    @Delete("/person/{person}/contact/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID person, @PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchemata()
                .map(schema -> Result.ok(contactDeleteUsecase.execute(schema, person, id)))
                .map(a ->  Result.ok(HttpResponse.noContent()))
                .recover(ErrorMapper::toHttpResponse);
    }
}
