package ru.pudgy.vertex.usecase.purpose;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Purpose;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.PurposeRepository;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class PurposeListUsecase {
    private final PurposeRepository purposeRepository;

    public List<Purpose> execute(Schemata schema) {
        return purposeRepository.findBySchemata(schema.getId());
    }
}
