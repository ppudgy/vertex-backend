package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.rest.mappers.NoteMapper;
import ru.pudgy.vertex.rest.mappers.StatisticMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.note.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class NoteCtrl {
    private final ListNoteUsecase listNoteUsecase;
    private final NoteByIdUsecase noteByIdUsecase;
    private final NoteCreateUsecase noteCreateUsecase;
    private final NoteUpdateUsecase noteUpdateUsecase;
    private final NoteDeleteUsecase noteDeleteUsecase;
    private final NoteStatisticUsecase noteStatisticUsecase;
    private final NoteMapper noteMapper;
    private final StatisticMapper statisticMapper;


    @Get(value = "/note", produces = MediaType.APPLICATION_JSON)
    HttpResponse<Page<NoteDto>> list(
            @Nullable Integer page,
            @Nullable Integer size,
            @Nullable  UUID purpose,
            @Nullable  String searchString
    ) {
        return SecurityHelper.currentSchema()
                .map(schema -> listNoteUsecase.execute(schema, page, size, purpose, searchString))
                .map(_page -> _page
                        .map(noteMapper::toDto)
                )
                .map(HttpResponse::ok)
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/note/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<NoteDto> topic(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> noteByIdUsecase.execute(schema, id))
                .map(noteMapper::toDto)
                .map(HttpResponse::ok)
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/note", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<NoteDto> create(@NotNull @Body NoteNewDto noteDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> noteCreateUsecase.execute(schema, noteMapper.toEntity(schema, noteDto)))
                .map(noteMapper::toDto)
                .map(HttpResponse::created)
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/note/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<NoteDto> update( @NotNull @PathVariable UUID id, @NotNull @Body NoteDto noteDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> noteUpdateUsecase.execute(schema, id, noteMapper.toEntity(schema, noteDto)))
                .map(noteMapper::toDto)
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/note/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> noteDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/note/purpose/statistic", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<StatisticDto>> statistic() {
        return SecurityHelper.currentSchema()
                .map(noteStatisticUsecase::execute)
                .map(statistics ->  statistics.stream()
                                                .map(statisticMapper::toDto)
                                                .collect(Collectors.toList())
                )
                .map(HttpResponse::ok)
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

}
