package ru.pudgy.vertex.usecase.purpose;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.PurposeUpdater;
import ru.pudgy.vertex.model.repository.PurposeRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PurposeUpdateUsecase {
    private final PurposeRepository purposeRepository;
    private final PurposeUpdater updater;

    public Purpose execute(Schemata schema, UUID id, Purpose newPurpose) {
        return purposeRepository.findBySchemataAndId(schema.getId(), id)
                .map(note -> updater.updateNote(newPurpose, note))
                .map(note -> purposeRepository.update(note))
                .orElseThrow(() -> new NotFoundException(String.format("purpose(%s) not found", id.toString())));

    }
}
