package ru.pudgy.vertex.rest.ctrl;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotAuthorizedException;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;
import ru.pudgy.vertex.rest.mappers.TopicMapper;
import ru.pudgy.vertex.rest.security.SecurityHelper;
import ru.pudgy.vertex.usecase.fragment.topic.*;
import ru.pudgy.vertex.usecase.topic.TopicListUsecase;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
public class FragmentTopicCtrl {
    private final ListFragmentTopicUsecase listFragmentTopicUsecase;
    private final FragmentTopicByIdUsecase fragmentTopicByIdUsecase;
    private final FragmentTopicCreateUsecase fragmentTopicCreateUsecase;
    private final FragmentTopicUpdateUsecase fragmentTopicUpdateUsecase;
    private final FragmentTopicDeleteUsecase fragmentTopicDeleteUsecase;
    private final TopicListUsecase topicListUsecase;

    private final TopicMapper topicMapper;

    @Get(value = "/document/{doc}/fragment/{fragment}/topic", produces = MediaType.APPLICATION_JSON)
    HttpResponse<List<TopicDto>> list(@NotNull UUID doc, @NotNull UUID fragment, @Nullable String searchString) {
        return SecurityHelper.currentSchema()
                .map(schema -> listFragmentTopicUsecase.execute(schema, doc, fragment, searchString))
                .map(list -> list.stream()
                        .map(topic -> topicMapper.toDto(topic))
                        .collect(Collectors.toList())
                )
                .map(list -> HttpResponse.ok(list))
                .orElseThrow(() -> new NotAuthorizedException(""));
    }
    @Get(value = "/document/{doc}/fragment/{fragment}/topic/{topic}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> fragment(@NotNull UUID doc, @NotNull UUID fragment, @NotNull UUID topic) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentTopicByIdUsecase.execute(schema, doc, fragment, topic))
                .map(top -> topicMapper.toDto(top))
                .map(dto -> HttpResponse.ok(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Put(value = "/document/{doc}/fragment/{fragment}/topic", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> create(@NotNull UUID doc, @NotNull UUID fragment, @NotNull @Body TopicDto topicDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentTopicCreateUsecase.execute(schema, doc, fragment, topicMapper.toEntity(schema, topicDto)))
                .map(top -> topicMapper.toDto(top))
                .map(dto -> HttpResponse.created(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }

    @Post(value = "/document/{doc}/fragment/{fragment}/topic/{topic}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    HttpResponse<TopicDto> update(@NotNull UUID doc,  @NotNull UUID fragment, @NotNull UUID topic, @NotNull @Body TopicDto topicDto) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentTopicUpdateUsecase.execute(schema, doc, fragment, topic, topicMapper.toEntity(schema, topicDto)))
                .map(top -> topicMapper.toDto(top))
                .map(dto -> HttpResponse.ok().body(dto))
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));

    }

    @Delete("/document/{doc}/fragment/{fragment}/topic/{topic}")
    HttpResponse<?> delete(@NotNull UUID doc, @NotNull UUID fragment, @NotNull UUID topic) {
        return SecurityHelper.currentSchema()
                .map(schema -> fragmentTopicDeleteUsecase.execute(schema, doc, fragment, topic))
                .map(a ->  HttpResponse.noContent())
                .orElseThrow(() -> new NotAuthorizedException("Not authorized"));
    }
}
