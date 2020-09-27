package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class Person {
    @Id
    private UUID id;
    private UUID schemata;
    private ZonedDateTime origin;
    private String name;
    private String sername;
    private String family;
    private LocalDate birthday;
    private String sex;
    private Boolean checked;
}
