package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.rest.dto.FragmentDto;
import ru.pudgy.vertex.rest.dto.FragmentNewDto;
import ru.pudgy.vertex.rest.mappers.FragmentMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.fragment.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class FragmentCtrl {
    private final ListFragmentUsecase listFragmentUsecase;
    private final FragmentByIdUsecase fragmentByIdUsecase;
    private final FragmentCreateUsecase fragmentCreateUsecase;
    private final FragmentUpdateUsecase fragmentUpdateUsecase;
    private final FragmentDeleteUsecase fragmentDeleteUsecase;
    private final FragmentMapper fragmentMapper;

    @Get(value = "/document/{doc}/fragment", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<FragmentDto>> list(@NotNull UUID doc) {
        return SecurityHelper.currentSchema()
                .map(schema -> listFragmentUsecase.execute(schema, doc))
                .map(list -> list.stream()
                        .map(fragment -> fragmentMapper.toDto(fragment))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok(list))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/document/{doc}/fragment/{fragment}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<FragmentDto> fragment(@NotNull UUID doc, @NotNull UUID fragment) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentByIdUsecase.execute(schema, doc, fragment))
                .map(frag -> fragmentMapper.toDto(frag))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/document/{doc}/fragment", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<FragmentDto> create(@NotNull UUID doc, @NotNull @Body FragmentNewDto fragmentNewDto) {
        Fragment newFragment = fragmentMapper.toEntity(fragmentNewDto);
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentCreateUsecase.execute(schema, doc, newFragment))
                .map(fragment -> fragmentMapper.toDto(fragment))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/document/{doc}/fragment/{fragment}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<FragmentDto> update(@NotNull UUID doc,  @NotNull UUID fragment, @NotNull @Body FragmentDto fragmentDto) {
        Fragment ufragment = fragmentMapper.toEntity(fragmentDto);
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentUpdateUsecase.execute(schema, doc, fragment, ufragment))
                .map(frag -> fragmentMapper.toDto(frag))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/document/{doc}/fragment/{fragment}")
    HttpResponse<?> delete(@NotNull UUID doc,  @NotNull UUID fragment) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentDeleteUsecase.execute(schema, doc, fragment))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
