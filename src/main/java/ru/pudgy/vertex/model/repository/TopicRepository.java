package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Topic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TopicRepository extends CrudRepository<Topic, UUID> {
    Optional<Topic> findBySchemataAndId(UUID schema, UUID id);
    List<Topic> findBySchemata(UUID schema);
    Number deleteBySchemataAndId(UUID schemata, UUID id);
    List<Topic> findBySchemataAndIdInOrderByName(UUID id, List<UUID> topics);


    @Query(value = "select t.id, t.schemata, t.name, " +
            "(select true from fragmenttopic ft  join fragment f on ft.fragment = f.id where f.id = :fragment and ft.topic = t.id) as checked " +
            " from topic t  " +
            " where " +
            "   t.schemata = :schema" +
            "   and (" +
            "       upper(name) like :name " +
            "       or (select true from fragmenttopic ft  join fragment f on ft.fragment = f.id where f.id = :fragment and ft.topic = t.id) " +
            "   )" +
            " order by checked desc, name", nativeQuery = true)
    List<Topic> findByFragmentAndNameLike(UUID schema, UUID fragment, String name);
}
