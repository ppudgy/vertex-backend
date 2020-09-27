package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Fragment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FragmentRepository  extends CrudRepository<Fragment, UUID> {
    List<Fragment> findBySchemataAndDocument(UUID schema, UUID doc);
    Optional<Fragment> findBySchemataAndDocumentAndId(UUID schema, UUID doc, UUID id);
    Number deleteBySchemataAndDocumentAndId(UUID schema, UUID doc, UUID id);

    @Query(value = "select fragment.* from fragment join fragmenttopic on fragment.id = fragmenttopic.fragment " +
            " where " +
            "    fragmenttopic.topic = :topic " +
            "   and fragment.schemata = :schema " +
            "  order by fragment.origin desc", nativeQuery = true)
    List<Fragment> findBySchemataAndTopic(UUID schema, UUID topic);
    @Query(value = "select fragment.* from fragment join fragmenttopic  on fragment.id = fragmenttopic.fragment" +
            " where " +
            "    fragmenttopic.topic = :topic " +
            "    and fragment.schemata = :schema " +
            "    and upper(fragment.text) like :searchString" +
            "   order by fragment.origin desc", nativeQuery = true)
    List<Fragment> findBySchemataAndTopicAndTextLike(UUID schema, UUID topic, String searchString);

    @Query(value = "select fragment.* from fragment join fragmentperson  on fragment.id = fragmentperson.fragment" +
            " where " +
            "     fragmentperson.person = :person " +
            "    and fragment.schemata = :schema " +
            "   order by fragment.origin desc", nativeQuery = true)
    List<Fragment> findBySchemataAndPerson(UUID schema, UUID person);

    @Query(value = "select fragment.* from fragment join fragmentperson  on fragment.id = fragmentperson.fragment" +
            " where " +
            "     fragmentperson.person = :person" +
            "    and fragment.schemata = :schema " +
            "    and upper(fragment.text) like :searchString" +
            "   order by fragment.origin desc", nativeQuery = true)
    List<Fragment> findBySchemataAndPersonAndTextLike(UUID schema, UUID person, String searchString);
}
