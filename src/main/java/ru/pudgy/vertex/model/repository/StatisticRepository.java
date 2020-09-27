package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.GenericRepository;
import ru.pudgy.vertex.model.entity.Statistic;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatisticRepository extends GenericRepository<Statistic, UUID> {
    @Query(value = "select purpose.id, purpose.name, purpose.color,  t.\"value\" " +
                    " from  purpose  left outer join  " +
                    "       (select purpose, count(*) as \"value\" from note where note.schemata = :schemaid group by purpose) t " +
                    "     on purpose.id = t.purpose " +
                    " where  " +
                    "    purpose.schemata = :schemaid " +
                    "    and purpose.active=true " +
                    " order by purpose.name", nativeQuery = true)
    List<Statistic> getNotePurposeStatistic(UUID schemaid);

    @Query(value = "select purpose.id, purpose.name, purpose.color,  t.\"value\" " +
                    " from  purpose  left outer join  " +
                    "       (select purpose, count(*) as \"value\" from todo where todo.schemata = :schemaid and todo.done = false group by purpose) t" +
                    "    on purpose.id = t.purpose " +
                    " where " +
                    "   purpose.schemata = :schemaid " +
                    "   and purpose.active=true " +
                    " order by purpose.name", nativeQuery = true)
    List<Statistic> getTodoPurposeStatistic(UUID schemaid);

    @Query(value = "select purpose.id, purpose.name, purpose.color,  t.\"value\" " +
                    " from  purpose  left outer join  " +
                    "        (select purpose, count(*) as \"value\" from document where document.schemata = :schemaid and document.treated = false group by purpose) t " +
                    "       on purpose.id = t.purpose " +
                    " where " +
                    "   purpose.schemata = :schemaid " +
                    "   and purpose.active=true " +
                    " order by purpose.name", nativeQuery = true)
    List<Statistic> getDocumentPurposeStatistic(UUID schemaid);
}
