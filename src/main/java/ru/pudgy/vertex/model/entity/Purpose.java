package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Purpose {
    @Id
    private UUID id;
    private UUID schemata;
    private String name;
    private String color;
    private Boolean active;
}
