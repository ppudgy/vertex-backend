package ru.pudgy.vertex.usecase.purpose;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PurposeRepository;

import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor
public class PurposeCreateUsecase {
    private final PurposeRepository purposeRepository;

    public Purpose execute(Schemata schema, Purpose newPurpose) {
        newPurpose.setSchemata(schema.getId());
        return purposeRepository.save(newPurpose);
    }
}
