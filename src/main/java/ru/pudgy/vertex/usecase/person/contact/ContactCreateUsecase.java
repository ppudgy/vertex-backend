package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.common.result.Result;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.errors.RuntimeVertexError;
import ru.pudgy.vertex.model.errors.SqlVertexError;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.model.mappers.ContactUpdater;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactCreateUsecase {
    private final ContactRepository contactRepository;
    private final ContactUpdater contactUpdater;

    public Result<Contact, VertexError> execute(Schemata schema, UUID person, Contact newContact) {
       try {
           return Result.ok(contactRepository.save(contactUpdater.createContact(schema, newContact)));
       } catch (RuntimeException e) {
           return Result.error(new RuntimeVertexError(e.getMessage()));
       }
    }

}
