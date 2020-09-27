package ru.pudgy.vertex.usecase.fragment.topic;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.FragmentTopic;
import ru.pudgy.vertex.model.entity.FragmentTopicKey;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.FragmentTopicRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentTopicCreateUsecase {
    private final FragmentTopicRepository fragmentTopicRepository;
    private final FragmentTopicByIdUsecase fragmentTopicByIdUsecase;

    public Topic execute(Schemata schema, UUID doc, UUID fragment, Topic newtopic) {
        FragmentTopicKey key = new FragmentTopicKey();
        key.setFragment(fragment);
        key.setTopic(newtopic.getId());
        FragmentTopic ft = new FragmentTopic();
        ft.setKey(key);
        ft = fragmentTopicRepository.save(ft);
        return fragmentTopicByIdUsecase.execute(schema, doc, fragment, newtopic.getId());
    }
}
