package ru.pudgy.vertex.usecase.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.PersonUpdater;
import ru.pudgy.vertex.model.repository.PersonRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PersonUpdateUsecase {
    private final PersonRepository personRepository;
    private final PersonUpdater updater;

    public Person execute(Schemata schema, UUID id, Person uperson) {
        return personRepository.findBySchemataAndId(schema.getId(), id)
                .map(person -> updater.updatePerson(uperson, person))
                .map(person -> personRepository.update(person))
                .orElseThrow(() -> new NotFoundException(String.format("person(%s) not found", id.toString())));
    }
}
