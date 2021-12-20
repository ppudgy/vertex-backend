package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.common.result.Result;
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

    public Result<Contact, VertexError> execute(Schemata schema, UUID person, UUID id) {
        return contactRepository.findBySchemataAndPersonAndId(schema.getId(), person, id)
                .map(Result::<Contact, VertexError>ok)
                .orElseGet(() -> Result.error(new EntityNotFoundVertexError<>(Contact.class, id)) );
    }

}
