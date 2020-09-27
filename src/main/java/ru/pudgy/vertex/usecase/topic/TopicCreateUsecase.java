package ru.pudgy.vertex.usecase.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.TopicRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TopicCreateUsecase {
    private final TopicRepository topicRepository;

    public Topic execute(@NotNull Schemata schema, @NotNull Topic newTopic) {
        newTopic.setId(UUID.randomUUID());
        newTopic.setSchemata(schema.getId());
        return topicRepository.save(newTopic);
    }
}
