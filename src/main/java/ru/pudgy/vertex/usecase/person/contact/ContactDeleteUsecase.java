package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactDeleteUsecase {
    private final ContactRepository contactRepository;

    public Number execute(Schemata schema, UUID person, UUID id) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .map(topic -> contactRepository.deleteBySchemataAndId(schema.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("contact(%s) for person(%s) not found", id.toString(), person.toString())));
    }
}
