package ru.pudgy.vertex.model.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import ru.pudgy.vertex.model.entity.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends CrudRepository<Contact, UUID> {
    List<Contact> findBySchemataAndPerson(UUID schema, UUID person);
    List<Contact> findBySchemataAndPersonAndContactIlike(UUID schema, UUID person, String contact);
    List<Contact> findBySchemataAndPersonAndTypeofcontact(UUID schema, UUID person, UUID type);
    List<Contact> findBySchemataAndPersonAndTypeofcontactAndContactIlike(UUID schema, UUID person, UUID type, String contact);
    Optional<Contact> findBySchemataAndPersonAndId(UUID schema, UUID person, UUID id);
    Number deleteBySchemataAndId(UUID schema, UUID id);
}
