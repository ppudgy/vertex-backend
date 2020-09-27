package ru.pudgy.vertex.usecase.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.repository.TopicRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TopicDeleteUsecase {
    private final TopicRepository topicRepository;

    public Number execute(@NotNull Schemata schema, @NotNull UUID id) {
        return topicRepository.findBySchemataAndId(schema.getId(), id)
                .map(topic -> topicRepository.deleteBySchemataAndId(schema.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("topic(%s) not found", id.toString())));
    }
}
