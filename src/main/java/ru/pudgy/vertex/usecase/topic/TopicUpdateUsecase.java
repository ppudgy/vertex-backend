package ru.pudgy.vertex.usecase.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.TopicRepository;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
@RequiredArgsConstructor
public class TopicUpdateUsecase {
    private final TopicRepository topicRepository;

    public Topic execute(@NotNull Schemata schema, @NotNull Topic topic) {
        topic.setSchemata(schema.getId());
        return topicRepository.update(topic);
    }
}
