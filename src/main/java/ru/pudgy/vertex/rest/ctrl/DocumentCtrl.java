package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.rest.dto.DocumentDto;
import ru.pudgy.vertex.rest.dto.StatisticDto;
import ru.pudgy.vertex.rest.mappers.DocumentMapper;
import ru.pudgy.vertex.rest.mappers.StatisticMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.document.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class DocumentCtrl {
    private final ListDocumentUsecase listDocumentUsecase;
    private final DocumentByIdUsecase documentByIdUsecase;
    private final DocumentCreateUsecase documentCreateUsecase;
    private final DocumentUpdateUsecase documentUpdateUsecase;
    private final DocumentDeleteUsecase documentDeleteUsecase;
    private final DocumentStatisticUsecase documentStatisticUsecase;
    private final DocumentMapper documentMapper;
    private final StatisticMapper statisticMapper;


    @Get(value = "/document", produces = MediaType.APPLICATION_JSON)
    HttpResponse<Page<DocumentDto>> list(
            @Nullable Integer page,
            @Nullable Integer size,
            @Nullable UUID purpose,
            @Nullable  String searchString
    ) {
        return SecurityHelper.currentSchema()
                .map(schema -> listDocumentUsecase.execute(schema, page, size, purpose, searchString))
                .map(_page -> _page
                        .map(document -> documentMapper.toDto(document))
                )
                .map(_page -> HttpResponse.ok(_page))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }

    @Get(value = "/document/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<DocumentDto> topic(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> documentByIdUsecase.execute(schema, id))
                .map(document -> documentMapper.toDto(document))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/document", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<DocumentDto> create(/*@NotNull @Body DocumentNewDto documentDto*/) {
        return HttpResponse.status(HttpStatus.METHOD_NOT_ALLOWED);
        /*
        Document newDocument = documentMapper.toEntity(documentDto);
        return SecurityHelper.currentSchema()
                .map(schema -> documentCreateUsecase.execute(schema, newDocument))
                .map(document -> documentMapper.toDto(document))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
         */
    }

    @Post(value = "/document/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<DocumentDto> update( @NotNull @PathVariable UUID id, @NotNull @Body DocumentDto documentDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> documentUpdateUsecase.execute(schema, id, documentMapper.toEntity(schema, documentDto)))
                .map(document -> documentMapper.toDto(document))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/document/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> documentDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/document/purpose/statistic", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<StatisticDto>> statistic() {
        return SecurityHelper.currentSchema()
                .map(schema -> documentStatisticUsecase.execute(schema))
                .map(statistics ->  statistics.stream()
                        .map(statistic -> statisticMapper.toDto(statistic))
                        .collect(Collectors.toList())
                )
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
