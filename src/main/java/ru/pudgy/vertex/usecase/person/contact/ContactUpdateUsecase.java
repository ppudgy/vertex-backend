package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.ContactUpdater;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactUpdateUsecase {
    private final ContactRepository contactRepository;
    private final ContactUpdater contactUpdater;

    public Contact execute(Schemata schema, UUID person, UUID id, Contact ucontact) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .map(contact -> contactUpdater.updateContact(ucontact, contact))
                .map(contact -> contactRepository.update(contact))
                .orElseThrow(() -> new NotFoundException(String.format("contact(%s) for person(%s) not found", id.toString(), person.toString())));
    }
}
