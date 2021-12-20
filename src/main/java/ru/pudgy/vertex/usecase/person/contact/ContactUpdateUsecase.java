package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.common.result.Result;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.errors.EntityNotFoundVertexError;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.model.mappers.ContactUpdater;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactUpdateUsecase {
    private final ContactRepository contactRepository;
    private final ContactUpdater contactUpdater;

    public Result<Contact, VertexError> execute(Schemata schema, UUID person, UUID id, Contact ucontact) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .map(contact -> contactUpdater.updateContact(ucontact, contact))
                .map(contact -> Result.<Contact, VertexError>ok(contactRepository.update(contact)))
                .orElseGet(() -> Result.error(new EntityNotFoundVertexError<>(Contact.class, id)));
    }
}
