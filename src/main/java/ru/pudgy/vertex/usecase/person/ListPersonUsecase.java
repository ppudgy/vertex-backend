package ru.pudgy.vertex.usecase.person;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PersonRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class ListPersonUsecase {
    private final PersonRepository personRepository;
    private final TextService textService;

    public Page<Person> execute(Schemata schema, Integer page, Integer size, String searchString) {
        Page<Person> result = Page.empty();
        Pageable pageable = Pageable.from(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(10)
        );
        searchString = textService.formatSearchString(searchString);
        if(searchString != null) {
            result = personRepository.findBySchemataAndTextLike(schema.getId(), searchString, pageable);
        } else {
            result = personRepository.findBySchemata(schema.getId(), pageable);
        }
        return result;
    }

    public List<Person> execute(Schemata schema, List<UUID> persons) {
        return personRepository.findBySchemataAndIdInOrderByName(schema.getId(), persons).stream()
                .map(topic -> {topic.setChecked(true); return topic;})
                .collect(Collectors.toList());
    }
}
