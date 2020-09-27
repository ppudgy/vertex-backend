package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
    Optional<Person> findBySchemataAndId(UUID schemata, UUID id);
    Number deleteBySchemataAndId(UUID schemata, UUID id);
    @Query(value = "from Person p where p.schemata=:schema and upper( COALESCE(p.family, ' ' ) || ' '  || COALESCE(p.name, ' ')  ||' ' ||  COALESCE(p.sername, ' ')) like upper(:name) order by p.family",
            countQuery = "from Person p where p.schemata=:schema and upper( COALESCE(p.family, ' ' ) || ' '  || COALESCE(p.name, ' ')  ||' ' ||  COALESCE(p.sername, ' ')) like upper(:name) order by p.family")
    Page<Person> findBySchemataAndTextLike(UUID schema, String name, Pageable pageable);
    Page<Person> findBySchemata(UUID schemata, Pageable pageable);
    @Query(value = "from Person p where p.schemata=:schema and id in :persons " +
            "order by upper( COALESCE(p.family, ' ' ) || ' '  || COALESCE(p.name, ' ')  ||' ' ||  COALESCE(p.sername, ' '))")
    List<Person> findBySchemataAndIdInOrderByName(UUID schema, List<UUID> persons);

    @Query(value = "select p.id, p.schemata, p.origin, p.name, p.sername, p.family, p.birthday, p.sex, " +
            "(select true from fragmentperson fp  where fp.fragment = :fragment and fp.person = p.id) as checked " +
            " from person p  " +
            " where " +
            " p.schemata = :schema " +
            " and (" +
            "       upper( COALESCE(p.family, ' ' ) || ' '  || COALESCE(p.name, ' ')  ||' ' ||  COALESCE(p.sername, ' ')) like :searchString " +
            "       or (select true from fragmentperson fp  where fp.fragment = :fragment and fp.person = p.id) " +
            "     )" +
            "order by checked, upper( COALESCE(p.family, ' ' ) || ' '  || COALESCE(p.name, ' ')  ||' ' ||  COALESCE(p.sername, ' '))", nativeQuery = true)
    List<Person> findBySchemataAndFragmentAndNameLike(UUID schema, UUID fragment, String searchString);
}
