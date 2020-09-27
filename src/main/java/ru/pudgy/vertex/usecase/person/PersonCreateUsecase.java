package ru.pudgy.vertex.usecase.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.PersonUpdater;
import ru.pudgy.vertex.model.repository.PersonRepository;

import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor
public class PersonCreateUsecase {
    private final PersonRepository personRepository;
    private final PersonUpdater personUpdater;

    public Person execute(Schemata schema, Person newPerson) {
        return personRepository.save(personUpdater.createPerson(schema, newPerson));
    }
}
