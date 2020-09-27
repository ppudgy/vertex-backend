package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.FragmentTopic;
import ru.pudgy.vertex.model.entity.FragmentTopicKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FragmentTopicRepository  extends CrudRepository<FragmentTopic, FragmentTopicKey> {
    @Query(value = "from FragmentTopic ft where ft.key.fragment = :fragment")
    List<FragmentTopic> findByFragment(UUID fragment);
    @Query(value = "from FragmentTopic ft where ft.key.topic = :topic")
    List<FragmentTopic> findByTopic(UUID topic);
    Optional<FragmentTopic> findByKey(FragmentTopicKey key);
}
