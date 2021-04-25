package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;
import ru.pudgy.vertex.rest.dto.TypeOfContactDto;
import ru.pudgy.vertex.rest.mappers.TypeOfContactMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactByIdUsecase;
import ru.pudgy.vertex.usecase.dictionary.TypeOfContactListUsecase;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TypeOfContactCtrl {
    private final TypeOfContactListUsecase typeOfContactListUsecase;
    private final TypeOfContactByIdUsecase typeOfContactByIdUsecase;
    private final TypeOfContactMapper typeOfContactMapper;


    @Get(value = "/typeofcontact", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<TypeOfContactDto>> list() {
        return SecurityHelper.currentSchema()
                .map(schema -> typeOfContactListUsecase.execute(schema))
                .map(list -> list.stream()
                        .map(typeOfContact -> typeOfContactMapper.toDto(typeOfContact))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok().body(list))
                //.orElseThrow(() -> new NotAuthorizedException("Not authorized"));
                .orElseGet(() -> HttpResponse.status(HttpStatus.UNAUTHORIZED));
    }

    @Get(value = "/typeofcontact/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<TypeOfContactDto> typeofcontact(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                //.map(schema -> typeOfContactByIdUsecase.execute(schema.getId(), id))
                .map(schema -> typeOfContactByIdUsecase.execute(schema.getId(), id))
                .map(typeOfContact -> typeOfContactMapper.toDto(typeOfContact))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/typeofcontact", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> create(@NotNull @Body TopicNewDto topicDto) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
/*        Topic newTopic = topicMapper.toEntity(topicDto);
        return SecurityHelper.currentSchema()
                .map(schema -> topicCreateUsecase.execute(schema, newTopic))
                .map(topic -> topicMapper.toDto(topic))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
 */
    }

    @Post(value = "/typeofcontact/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> update( @NotNull @PathVariable UUID id, @NotNull @Body TopicDto topicDto) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
        /*
        Topic newTopic = topicMapper.toEntity(topicDto);
        return SecurityHelper.currentSchema()
                .map(schema -> topicUpdateUsecase.execute(schema, newTopic))
                .map(topic -> topicMapper.toDto(topic))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

         */
    }

    @Delete("/typeofcontact/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
        /*
        return SecurityHelper.currentSchema()
                .map(schema -> topicDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

         */
    }
}
