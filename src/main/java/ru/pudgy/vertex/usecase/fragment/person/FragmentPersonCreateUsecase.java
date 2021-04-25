package ru.pudgy.vertex.usecase.fragment.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.*;
import ru.pudgy.vertex.model.repository.FragmentPersonRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentPersonCreateUsecase {
    private final FragmentPersonRepository fragmentPersonRepository;
    private final FragmentPersonByIdUsecase fragmentPersonByIdUsecase;

    public Person execute(Schemata schema, UUID doc, UUID fragment, Person newperson) {
        FragmentPersonKey key = new FragmentPersonKey();
        key.setFragment(fragment);
        key.setPerson(newperson.getId());
        FragmentPerson ft = new FragmentPerson(key);
        //ft.setKey(key);
        ft = fragmentPersonRepository.save(ft);
        return fragmentPersonByIdUsecase.execute(schema, doc, fragment, newperson.getId());
    }
}
