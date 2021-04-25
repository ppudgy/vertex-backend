package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "typeofcontact")
@Data
public class TypeOfContact {
    @Id
    private UUID id;
    private String name;
}
