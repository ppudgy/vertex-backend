package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.rest.dto.PersonDto;
import ru.pudgy.vertex.rest.mappers.PersonMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.fragment.person.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class FragmentPersonCtrl {
    private final ListFragmentPersonUsecase listFragmentPersonUsecase;
    private final FragmentPersonByIdUsecase fragmentPersonByIdUsecase;
    private final FragmentPersonCreateUsecase fragmentPersonCreateUsecase;
    private final FragmentPersonUpdateUsecase fragmentPersonUpdateUsecase;
    private final FragmentPersonDeleteUsecase fragmentPersonDeleteUsecase;

    private final PersonMapper personMapper;

    @Get(value = "/document/{doc}/fragment/{fragment}/person", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<PersonDto>> list(@NotNull UUID doc, @NotNull UUID fragment, @Nullable String searchString) {
        return SecurityHelper.currentSchema()
                .map(schema -> listFragmentPersonUsecase.execute(schema, doc, fragment, searchString))
                .map(list -> list.stream()
                        .map(person -> personMapper.toDto(person))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok(list))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/document/{doc}/fragment/{fragment}/person/{person}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> fragment(@NotNull UUID doc, @NotNull UUID fragment, @NotNull UUID person) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentPersonByIdUsecase.execute(schema, doc, fragment, person))
                .map(top -> personMapper.toDto(top))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/document/{doc}/fragment/{fragment}/person", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> create(@NotNull UUID doc, @NotNull UUID fragment, @NotNull @Body PersonDto personDto){
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentPersonCreateUsecase.execute(schema, doc, fragment, personMapper.toEntity(schema, personDto)))
                .map(top -> personMapper.toDto(top))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/document/{doc}/fragment/{fragment}/person/{person}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PersonDto> update(@NotNull UUID doc,  @NotNull UUID fragment, @NotNull UUID person, @NotNull @Body PersonDto personDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentPersonUpdateUsecase.execute(schema, doc, fragment, person, personMapper.toEntity(schema, personDto)))
                .map(top -> personMapper.toDto(top))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

    }

    @Delete("/document/{doc}/fragment/{fragment}/person/{person}")
    HttpResponse<?> delete(@NotNull UUID doc, @NotNull UUID fragment, @NotNull UUID person) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentPersonDeleteUsecase.execute(schema, doc, fragment, person))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
