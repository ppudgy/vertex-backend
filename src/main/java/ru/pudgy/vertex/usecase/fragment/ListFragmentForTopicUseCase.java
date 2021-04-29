package ru.pudgy.vertex.usecase.fragment;

import io.micronaut.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListFragmentForTopicUseCase {
    private final FragmentRepository fragmentRepository;
    private final TextService textService;

    public List<Fragment> execute(Schemata schema, UUID topic, String searchString) {

        return textService.formatSearchString(searchString)
                .map(formatString -> fragmentRepository.findBySchemataAndTopicAndTextIlike(schema.getId(), topic, formatString))
                .orElseGet(() -> fragmentRepository.findBySchemataAndTopic(schema.getId(), topic));
    }
}
