package ru.pudgy.vertex.usecase.dictionary;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.model.repository.TypeOfContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class TypeOfContactByIdUsecase {
    private final TypeOfContactRepository typeOfContactRepository;

    public TypeOfContact execute(UUID schema, UUID id) {
        return typeOfContactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("type of contact(%s) not found", id.toString())));
    }
}
