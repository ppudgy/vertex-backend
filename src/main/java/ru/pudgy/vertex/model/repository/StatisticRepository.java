package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.GenericRepository;
import ru.pudgy.vertex.model.entity.Statistic;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatisticRepository extends GenericRepository<Statistic, UUID> {
    @Query(value = "select p.id, p.name, p.color,  t.\"value\" " +
                    " from  purpose p left outer join  " +
                    "       (select note.purpose, count(*) as \"value\" from note where note.schemata = :schemaid group by note.purpose) t " +
                    "     on p.id = t.purpose " +
                    " where  " +
                    "    p.schemata = :schemaid1 " +
                    "    and p.active=true " +
                    " order by p.name", nativeQuery = true)
    List<Statistic> getNotePurposeStatistic(UUID schemaid, UUID schemaid1);

    @Query(value = "select purpose.id, purpose.name, purpose.color,  t.\"value\" " +
                    " from  purpose  left outer join  " +
                    "       (select todo.purpose, count(*) as \"value\" from todo where todo.schemata = :schemaid and todo.done = false group by purpose) t" +
                    "    on purpose.id = t.purpose " +
                    " where " +
                    "   purpose.schemata = :schemaid1 " +
                    "   and purpose.active=true " +
                    " order by purpose.name", nativeQuery = true)
    List<Statistic> getTodoPurposeStatistic(UUID schemaid, UUID schemaid1);

    @Query(value = "select purpose.id, purpose.name, purpose.color,  t.\"value\" " +
                    " from  purpose  left outer join  " +
                    "        (select document.purpose, count(*) as \"value\" from document where document.schemata = :schemaid and document.treated = false group by purpose) t " +
                    "       on purpose.id = t.purpose " +
                    " where " +
                    "   purpose.schemata = :schemaid1 " +
                    "   and purpose.active=true " +
                    " order by purpose.name", nativeQuery = true)
    List<Statistic> getDocumentPurposeStatistic(UUID schemaid, UUID schemaid1);
}
