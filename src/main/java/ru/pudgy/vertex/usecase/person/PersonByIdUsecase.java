package ru.pudgy.vertex.usecase.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Person;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PersonRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PersonByIdUsecase {
    private final PersonRepository personRepository;

    public Person execute(Schemata schema, UUID id) {
        return personRepository.findBySchemataAndId(schema.getId(), id)
                .orElseThrow(() -> new NotFoundException(String.format("person(%s) not found", id.toString())));
    }
}
