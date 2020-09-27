package ru.pudgy.vertex.usecase.fragment.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.*;
import ru.pudgy.vertex.model.repository.FragmentPersonRepository;
import ru.pudgy.vertex.model.repository.FragmentTopicRepository;
import ru.pudgy.vertex.usecase.fragment.topic.FragmentTopicByIdUsecase;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentPersonUpdateUsecase {
    private final FragmentPersonRepository fragmentPersonRepository;
    private final FragmentPersonByIdUsecase fragmentPersonByIdUsecase;

    public Person execute(Schemata schema, UUID doc, UUID fragment, UUID person, Person uperson) {
        FragmentPersonKey key = new FragmentPersonKey(fragment, person);
        if(uperson.getChecked()) {
            FragmentPerson fragmentPerson = new FragmentPerson(key);
            fragmentPersonRepository.save(fragmentPerson);
        } else {
            fragmentPersonRepository.deleteById(key);
        }
        //return fragmentPersonByIdUsecase.execute(schema, doc, fragment, topic);
        return uperson;
    }
}
