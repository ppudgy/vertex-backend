package ru.pudgy.vertex.usecase.fragment.person;

import io.micronaut.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentPersonRepository;
import ru.pudgy.vertex.model.repository.PersonRepository;
import ru.pudgy.vertex.srvc.TextService;
import ru.pudgy.vertex.usecase.person.ListPersonUsecase;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor
public class ListFragmentPersonUsecase {
    private final FragmentPersonRepository fragmentPersonRepository;
    private final PersonRepository personRepository;
    private final ListPersonUsecase listPersonUsecase;
    private final TextService textService;

    public List<Person> execute(Schemata schema, UUID doc, UUID fragment, String searchString) {
        searchString = textService.formatSearchString(searchString);
        if(StringUtils.isEmpty(searchString)) {
            List<UUID> persons = fragmentPersonRepository.findByFragment(fragment).stream()
                    .map(ft -> ft.getKey().getPerson())
                    .collect(Collectors.toList());
            return listPersonUsecase.execute(schema, persons);
        } else {
            List<Person> res = personRepository.findBySchemataAndFragmentAndNameIlike(schema.getId(), fragment, fragment, searchString);
            return res;
        }
    }
}
