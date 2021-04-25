package ru.pudgy.vertex.usecase.fragment.topic;

import io.micronaut.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Topic;
import ru.pudgy.vertex.model.repository.FragmentTopicRepository;
import ru.pudgy.vertex.model.repository.TopicRepository;
import ru.pudgy.vertex.srvc.TextService;
import ru.pudgy.vertex.usecase.topic.TopicListUsecase;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class ListFragmentTopicUsecase {
    private final FragmentTopicRepository fragmentTopicRepository;
    private final TopicRepository topicRepository;
    private final TopicListUsecase topicListUsecase;
    private final TextService textService;

    public List<Topic> execute(Schemata schema, UUID doc, UUID fragment, String searchString) {
        searchString = textService.formatSearchString(searchString);
        if(StringUtils.isEmpty(searchString)) {
            List<UUID> topics = fragmentTopicRepository.findByFragment(fragment).stream()
                    .map(ft -> ft.getKey().getTopic())
                    .collect(Collectors.toList());
            return topicListUsecase.execute(schema, topics);
        } else {
            List<Topic> res = topicRepository.findByFragmentAndNameIlike(schema.getId(), fragment, fragment, searchString);
            return res;
        }
    }
}
