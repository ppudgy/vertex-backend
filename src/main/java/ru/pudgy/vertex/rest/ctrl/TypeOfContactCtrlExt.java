package ru.pudgy.vertex.rest.ctrl;

import com.jnape.palatable.lambda.adt.Either;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.result.Result;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;
import ru.pudgy.vertex.rest.dto.TypeOfContactDto;
import ru.pudgy.vertex.rest.mappers.ErrorMapper;
import ru.pudgy.vertex.rest.mappers.TypeOfContactMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactByIdUsecase;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactListUsecase;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("/ext")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TypeOfContactCtrlExt {
    private final TypeOfContactListUsecase typeOfContactListUsecase;
    private final TypeOfContactByIdUsecase typeOfContactByIdUsecase;
    private final TypeOfContactMapper typeOfContactMapper;

    @Get(value = "/typeofcontact", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> list() {
        return SecurityHelper.currentSchemata()
                .map(schema -> typeOfContactListUsecase.execute())
                .map(list -> Result.ok(list.stream()
                        .map(typeOfContactMapper::toDto)
                        .collect(Collectors.toList()))
                )
                .map(list -> Result.ok(HttpResponse.ok().body(list)))
                .recover(ErrorMapper::toHttpResponse);
    }

    @Get(value = "/typeofcontact/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> typeofcontact(@PathVariable @NotNull UUID id) {
        var result = SecurityHelper.currentSchemata()
                .map(schema -> typeOfContactByIdUsecase.execute(schema, id))
                .map(typeOfContact -> Result.ok(typeOfContactMapper.toDto(typeOfContact)))
                .map(dto -> Result.ok(HttpResponse.ok().body(dto)))
                .recover(ErrorMapper::toHttpResponse);


        return result;
    }


    @Put(value = "/typeofcontact", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> create(@NotNull @Body TopicNewDto topicDto) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Post(value = "/typeofcontact/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> update( @NotNull @PathVariable UUID id, @NotNull @Body TopicDto topicDto) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Delete("/typeofcontact/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
