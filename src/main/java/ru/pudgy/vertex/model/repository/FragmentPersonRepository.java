package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.FragmentPerson;
import ru.pudgy.vertex.model.entity.FragmentPersonKey;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FragmentPersonRepository extends CrudRepository<FragmentPerson, FragmentPersonKey> {
    @Query(value = "from FragmentPerson ft where ft.key.fragment = :fragment")
    List<FragmentPerson> findByFragment(UUID fragment);
    @Query(value = "from FragmentPerson ft where ft.key.topic = :topic")
    List<FragmentPerson> findByPerson(UUID topic);
    Optional<FragmentPerson> findByKey(FragmentPersonKey key);
}
