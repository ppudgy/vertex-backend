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

    public Result<List<Contact>, VertexError> execute(Schemata schema, UUID person, UUID type, String searchString) {
        List<Contact> result = textService.formatSearchString(searchString)
                .map(formatString -> {
                    if (type != null) {
                        return contactRepository.findBySchemataAndPersonAndTypeofcontactAndContactIlike(schema.getId(), person, type, formatString);
                    } else{
                        return contactRepository.findBySchemataAndPersonAndContactIlike(schema.getId(), person, formatString);
                    }
                })
                .orElseGet(()->{
                    if (type != null) {
                        return contactRepository.findBySchemataAndPersonAndTypeofcontact(schema.getId(), person, type);
                    } else {
                        return contactRepository.findBySchemataAndPerson(schema.getId(), person);
                    }
                });
        return Result.ok(result);
    }
}
