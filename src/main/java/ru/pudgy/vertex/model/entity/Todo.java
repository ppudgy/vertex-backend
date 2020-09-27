package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class Todo {
    @Id
    private UUID                id;
    private ZonedDateTime       origin;             // Время создания
    private UUID                schemata;
    private UUID				purpose;
    private Boolean             external;           //Зависимость от внешних факторов (людей): зависит, независит
    private Boolean             done;               // признак окончания
    private ZonedDateTime       relevance;          //Сроком актуальности
    private ZonedDateTime       endtime;                // Окончание выполнения
    private String              description;
    private Integer             auto;
    private Integer             autoperiod;
}
