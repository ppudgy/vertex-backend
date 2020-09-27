package ru.pudgy.vertex.usecase.fragment.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.FragmentPersonKey;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentPersonRepository;
import ru.pudgy.vertex.usecase.person.PersonByIdUsecase;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentPersonByIdUsecase {
    private final FragmentPersonRepository fragmentPersonRepository;
    private final PersonByIdUsecase personByIdUsecase;

    public Person execute(Schemata schema, UUID doc, UUID fragment, UUID person) {
        FragmentPersonKey k = new FragmentPersonKey();
        k.setFragment(fragment);
        k.setPerson(person);
        return fragmentPersonRepository.findByKey(k)
                .map(ft -> personByIdUsecase.execute(schema, ft.getKey().getPerson()))
                .orElseThrow(() -> new NotFoundException(String.format("fragment(%s)person(%s) not found", fragment.toString(), person.toString())));
    }
}
