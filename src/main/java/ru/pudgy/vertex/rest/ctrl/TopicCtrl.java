package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.rest.dto.FragmentDto;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;
import ru.pudgy.vertex.rest.mappers.FragmentMapper;
import ru.pudgy.vertex.rest.mappers.TopicMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.fragment.ListFragmentForTopicUseCase;
import ru.pudgy.vertex.usecase.topic.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TopicCtrl {
    private final TopicDeleteUsecase topicDeleteUsecase;
    private final TopicListUsecase topicListUsecase;
    private final TopicByIdUsecase topicByIdUsecase;
    private final TopicCreateUsecase topicCreateUsecase;
    private final TopicUpdateUsecase topicUpdateUsecase;
    private final ListFragmentForTopicUseCase listFragmentForTopicUseCase;
    private final TopicMapper topicMapper;
    private final FragmentMapper fragmentMapper;

    @Get(value = "/topic", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<TopicDto>> list() {
        return SecurityHelper.currentSchema()
                .map(schema -> topicListUsecase.execute(schema))
                .map(list -> list.stream()
                                    .map(topic->topicMapper.toDto(topic))
                                    .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok().body(list))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/topic/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> topic(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> topicByIdUsecase.execute(schema, id))
                .map(topic -> topicMapper.toDto(topic))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/topic", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> create(@NotNull @Body TopicNewDto topicDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> topicCreateUsecase.execute(schema, topicMapper.toEntity(schema, topicDto)))
                .map(topic -> topicMapper.toDto(topic))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/topic/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> update( @NotNull @PathVariable UUID id, @NotNull @Body TopicDto topicDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> topicUpdateUsecase.execute(schema, topicMapper.toEntity(schema, topicDto)))
                .map(topic -> topicMapper.toDto(topic))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Delete("/topic/{id}")
    HttpResponse<?> delete(@PathVariable @NotNull UUID id) {
        return SecurityHelper.currentSchema()
                .map(schema -> topicDeleteUsecase.execute(schema, id))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Get(value = "/topic/{id}/fragment", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<FragmentDto>> topic(@PathVariable @NotNull UUID id, @Nullable String searchString){
        return SecurityHelper.currentSchema()
                .map(schema -> listFragmentForTopicUseCase.execute(schema, id, searchString))
                .map(list -> list.stream()
                                .map(fragment -> fragmentMapper.toDto(fragment))
                                .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok().body(list))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
