package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.rest.dto.FragmentDto;
import ru.pudgy.vertex.rest.dto.PersonDto;
import ru.pudgy.vertex.rest.dto.PersonNewDto;
import ru.pudgy.vertex.rest.mappers.FragmentMapper;
import ru.pudgy.vertex.rest.mappers.PersonMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.fragment.ListFragmentForPersonUseCase;
import ru.pudgy.vertex.usecase.person.*;


import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PersonCtrl {
    private final ListPersonUsecase listPersonUsecase;
    private final PersonByIdUsecase personByIdUsecase;
    private final PersonCreateUsecase personCreateUsecase;
    private final PersonUpdateUsecase personUpdateUsecase;
    private final PersonDeleteUsecase personDeleteUsecase;
    private final ListFragmentForPersonUseCase listFragmentForPersonUseCase;
    private final PersonMapper personMapper;
    private final FragmentMapper fragmentMapper;


    @Get(value = "/person", produces = MediaType.APPLICATION_JSON)
    HttpResponse<Page<PersonDto>> list(
            @Nullable Integer page,
            @Nullable Integer size,
            @Nullable  String searchString
    ) {
        return SecurityHelper.currentSchema()
                .map(schema -> listPersonUsecase.execute(schema, page, size, searchString))
                .map(_page -> _page
                        .map(person -> personMapper.toDto(person))
                )
                .map(_page -> HttpResponse.ok(_page))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/person/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> topic(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> personByIdUsecase.execute(schema, id))
                .map(person -> personMapper.toDto(person))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/person", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> create(@NotNull @Body PersonNewDto personDto) {
        Person newPerson = personMapper.toEntity(personDto);
        return SecurityHelper.currentSchema()
                .map(schema -> personCreateUsecase.execute(schema, newPerson))
                .map(person -> personMapper.toDto(person))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/person/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> update( @NotNull @PathVariable UUID id, @NotNull @Body PersonDto personDto) {
        Person uperson = personMapper.toEntity(personDto);
        return SecurityHelper.currentSchema()
                .map(schema -> personUpdateUsecase.execute(schema, id, uperson))
                .map(person -> personMapper.toDto(person))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/person/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> personDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/person/{id}/fragment", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<FragmentDto>> topic(@PathVariable @NotNull UUID id, @Nullable String searchString) {
        return SecurityHelper.currentSchema()
                .map(schema -> listFragmentForPersonUseCase.execute(schema, id, searchString))
                .map(list -> list.stream()
                        .map(fragment -> fragmentMapper.toDto(fragment))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok().body(list))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
