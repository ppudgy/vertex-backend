package ru.pudgy.vertex.usecase.fragment.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.FragmentTopicKey;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentTopicRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentTopicDeleteUsecase {
    private final FragmentTopicRepository fragmentTopicRepository;

    // TODO sure to use schema to prevent delete alien fragmentTopic
    public Number execute(Schemata schema, UUID doc, UUID fragment, UUID topic) {
        FragmentTopicKey key = new FragmentTopicKey(fragment, topic);
        fragmentTopicRepository.deleteById(key);
        return 1;
    }
}
