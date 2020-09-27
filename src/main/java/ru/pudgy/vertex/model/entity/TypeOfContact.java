package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class TypeOfContact {
    @Id
    private UUID id;
    private String name;
}
