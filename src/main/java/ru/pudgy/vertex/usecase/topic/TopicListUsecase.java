package ru.pudgy.vertex.usecase.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.TopicRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Singleton
@RequiredArgsConstructor
public class TopicListUsecase {
    private final TopicRepository topicRepository;
    private final TextService textService;

    public List<Topic> execute(@NotNull Schemata schema) {
        return topicRepository.findBySchemata(schema.getId());
    }

    public List<Topic> execute(Schemata schema, List<UUID> topics) {
        return topicRepository.findBySchemataAndIdInOrderByName(schema.getId(), topics).stream()
                .map(topic -> {topic.setChecked(true); return topic;})
                .collect(Collectors.toList());
    }
}
