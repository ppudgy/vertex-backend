package ru.pudgy.vertex.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {
    @Id
    private UUID id;
    private UUID schemata;
    private String name;
    private Boolean checked;
}
