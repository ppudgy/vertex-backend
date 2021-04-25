package ru.pudgy.vertex.usecase.document;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.Statistic;
import ru.pudgy.vertex.model.repository.StatisticRepository;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class DocumentStatisticUsecase {
    private final StatisticRepository statisticRepository;

    public List<Statistic> execute(Schemata schema) {
        return statisticRepository.getDocumentPurposeStatistic(schema.getId(), schema.getId());
    }
}
