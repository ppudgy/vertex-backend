package ru.pudgy.vertex.usecase.person.contact;

import lombok.RequiredArgsConstructor;
import ru.pudgy.result.Result;
import ru.pudgy.vertex.model.entity.Contact;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.model.repository.ContactRepository;
import ru.pudgy.vertex.srvc.TextService;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListContactUsecase {
    private final ContactRepository contactRepository;
    private final TextService textService;

    public List<Contact> execute(Schemata schema, UUID person, UUID type, String searchString) {
        List<Contact> result = Collections.emptyList();
        searchString = textService.formatSearchString(searchString);

        if (type != null && searchString != null) {
            result = contactRepository.findBySchemataAndPersonAndTypeofcontactAndContactIlike(schema.getId(), person, type, searchString);
        } else if (type != null) {
            result = contactRepository.findBySchemataAndPersonAndTypeofcontact(schema.getId(), person, type);
        } else if (searchString != null) {
            result = contactRepository.findBySchemataAndPersonAndContactIlike(schema.getId(), person, searchString);
        } else {
            result = contactRepository.findBySchemataAndPerson(schema.getId(), person);
        }
        return result;
    }


    public Result<List<Contact>, VertexError> executeR(Schemata schema, UUID person, UUID type, String searchString) {
        List<Contact> result = Collections.emptyList();
        searchString = textService.formatSearchString(searchString);

        if (type != null && searchString != null) {
            result = contactRepository.findBySchemataAndPersonAndTypeofcontactAndContactIlike(schema.getId(), person, type, searchString);
        } else if (type != null) {
            result = contactRepository.findBySchemataAndPersonAndTypeofcontact(schema.getId(), person, type);
        } else if (searchString != null) {
            result = contactRepository.findBySchemataAndPersonAndContactIlike(schema.getId(), person, searchString);
        } else {
            result = contactRepository.findBySchemataAndPerson(schema.getId(), person);
        }
        return Result.ok(result);
    }
}
