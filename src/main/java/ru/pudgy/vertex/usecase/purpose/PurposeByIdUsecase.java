package ru.pudgy.vertex.usecase.purpose;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PurposeRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PurposeByIdUsecase {
    private final PurposeRepository purposeRepository;

    public Purpose execute(UUID schema, UUID id) {
        return purposeRepository.findBySchemataAndId(schema, id)
                .orElseThrow(() -> new NotFoundException(String.format("purpose(%s) not found", id.toString())));
    }
}
