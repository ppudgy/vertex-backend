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
public class FragmentTopicUpdateUsecase {
    private final FragmentTopicRepository fragmentTopicRepository;
    private final FragmentTopicByIdUsecase fragmentTopicByIdUsecase;

    public Topic execute(Schemata schema, UUID doc, UUID fragment, UUID topic, Topic utopic) {
        FragmentTopicKey key = new FragmentTopicKey(fragment, topic);
        if(utopic.getChecked()) {
            FragmentTopic fragmentTopic = new FragmentTopic(key);
            fragmentTopicRepository.save(fragmentTopic);
        } else {
            fragmentTopicRepository.deleteById(key);
        }
        //return fragmentTopicByIdUsecase.execute(schema, doc, fragment, topic);
        return utopic;
    }
}
