package ru.pudgy.vertex.usecase.purpose;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PurposeRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PurposeDeleteUsecase {
    private final PurposeRepository purposeRepository;

    public Number execute(Schemata schema, UUID id) {
        return purposeRepository.findBySchemataAndId(schema.getId(), id)
                .map(note -> purposeRepository.deleteBySchemataAndId(schema.getId(), id))
                .orElseThrow(() -> new NotFoundException(String.format("note(%s) not found", id.toString())));
    }
}
