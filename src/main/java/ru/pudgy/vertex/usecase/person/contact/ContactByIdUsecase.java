package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.result.Result;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.errors.EntityNotFoundVertexError;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.model.repository.ContactRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ContactByIdUsecase {
    private final ContactRepository contactRepository;

    public Contact execute(Schemata schema, UUID person, UUID id) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .orElseThrow(() -> new NotFoundException(String.format("contact(%s) for person(%s) not found", id.toString(), person.toString())));
    }

    public Result<Contact, VertexError> executeR(Schemata schema, UUID person, UUID id) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .map(Result::<Contact, VertexError>ok)
                .orElseGet(() -> Result.error(new EntityNotFoundVertexError<>(Contact.class, id)) );
    }

}
