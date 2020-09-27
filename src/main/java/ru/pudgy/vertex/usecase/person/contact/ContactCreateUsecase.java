package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.ContactUpdater;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactCreateUsecase {
    private final ContactRepository contactRepository;
    private final ContactUpdater contactUpdater;

    public Contact execute(Schemata schema, UUID person, Contact newContact) {
        return contactRepository.save(contactUpdater.createContact(schema, newContact));
    }
}
