package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Statistic {
    @Id
    UUID id;
    String name;
    String color;
    Integer value;
}
