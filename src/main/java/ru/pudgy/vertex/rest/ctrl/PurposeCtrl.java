package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.rest.dto.PurposeDto;
import ru.pudgy.vertex.rest.dto.PurposeNewDto;
import ru.pudgy.vertex.rest.mappers.PurposeMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.purpose.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PurposeCtrl {
    private final PurposeDeleteUsecase purposeDeleteUsecase;
    private final PurposeListUsecase purposeListUsecase;
    private final PurposeByIdUsecase purposeByIdUsecase;
    private final PurposeCreateUsecase purposeCreateUsecase;
    private final PurposeUpdateUsecase purposeUpdateUsecase;
    private final PurposeMapper purposeMapper;

    @Get(value = "/purpose", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<PurposeDto>> list() {
        return SecurityHelper.currentSchema()
                .map(schema -> purposeListUsecase.execute(schema))
                .map(list -> list.stream()
                        .map(purpose->purposeMapper.toDto(purpose))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok().body(list))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/purpose/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<PurposeDto> purpose(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> purposeByIdUsecase.execute(schema.getId(), id))
                .map(purpose -> purposeMapper.toDto(purpose))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/purpose", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PurposeDto> create(@NotNull @Body PurposeNewDto purposeDto) {
        Purpose newPurpose = purposeMapper.toEntity(purposeDto);
        return SecurityHelper.currentSchema()
                .map(schema -> purposeCreateUsecase.execute(schema, newPurpose))
                .map(purpose -> purposeMapper.toDto(purpose))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/purpose/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<PurposeDto> update( @NotNull @PathVariable UUID id, @NotNull @Body PurposeDto purposeDto) {
        Purpose newPurpose = purposeMapper.toEntity(purposeDto);
        return SecurityHelper.currentSchema()
                .map(schema -> purposeUpdateUsecase.execute(schema, id, newPurpose))
                .map(purpose -> purposeMapper.toDto(purpose))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

    }

    @Delete("/purpose/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> purposeDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
