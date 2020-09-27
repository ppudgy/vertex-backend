package ru.pudgy.vertex.usecase.fragment.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.FragmentTopicKey;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.FragmentTopicRepository;
import ru.pudgy.vertex.usecase.topic.TopicByIdUsecase;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentTopicByIdUsecase {
    private final FragmentTopicRepository fragmentTopicRepository;
    private final TopicByIdUsecase topicByIdUsecase;

    public Topic execute(Schemata schema, UUID doc, UUID fragment, UUID topic) {
        FragmentTopicKey k = new FragmentTopicKey();
        k.setFragment(fragment);
        k.setTopic(topic);
        return fragmentTopicRepository.findByKey(k)
                .map(ft -> topicByIdUsecase.execute(schema, ft.getKey().getTopic()))
                .orElseThrow(() -> new NotFoundException(String.format("fragment(%s)topic(%s) not found", fragment.toString(), topic.toString())));
    }
}
