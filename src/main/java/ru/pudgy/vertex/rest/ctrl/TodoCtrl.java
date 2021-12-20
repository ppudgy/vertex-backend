package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.rest.mappers.StatisticMapper;
import ru.pudgy.vertex.rest.mappers.TodoMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.todo.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TodoCtrl {
    private final ListTodoUsecase listTodoUsecase;
    private final TodoByIdUsecase todoByIdUsecase;
    private final TodoCreateUsecase todoCreateUsecase;
    private final TodoUpdateUsecase todoUpdateUsecase;
    private final TodoDeleteUsecase todoDeleteUsecase;
    private final TodoStatisticUsecase todoStatisticUsecase;
    private final TodoMapper todoMapper;
    private final StatisticMapper statisticMapper;

    @Get(value = "/todo", produces = MediaType.APPLICATION_JSON)
    HttpResponse<Page<TodoDto>> list(
            @Nullable Integer page,
            @Nullable Integer size,
            @Nullable UUID purpose,
            @Nullable  String searchString
    ) {
        return SecurityHelper.currentSchema()
                .map(schema -> listTodoUsecase.execute(schema, page, size, purpose, searchString))
                .map(_page -> _page
                        .map(todo -> todoMapper.toDto(todo))
                )
                .map(_page -> HttpResponse.ok(_page))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<TodoDto> topic(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> todoByIdUsecase.execute(schema, id))
                .map(todo -> todoMapper.toDto(todo))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/todo", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TodoDto> create(@NotNull @Body TodoNewDto todoDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> todoCreateUsecase.execute(schema, todoMapper.toEntity(schema, todoDto)))
                .map(todo -> todoMapper.toDto(todo))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/todo/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TodoDto> update( @NotNull @PathVariable UUID id, @NotNull @Body TodoDto todoDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> todoUpdateUsecase.execute(schema, id, todoMapper.toEntity(schema,todoDto)))
                .map(todo -> todoMapper.toDto(todo))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/todo/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> todoDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/todo/purpose/statistic", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<StatisticDto>> statistic() {
        return SecurityHelper.currentSchema()
                .map(schema -> todoStatisticUsecase.execute(schema))
                .map(statistics ->  statistics.stream()
                        .map(statistic -> statisticMapper.toDto(statistic))
                        .collect(Collectors.toList())
                )
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
