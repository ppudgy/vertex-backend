package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class Document {
    @Id
    private UUID           id;           // Сурогатный ключ
    private ZonedDateTime  origin;       // Дата и время внесения информации в банк данных
    private UUID           schemata;       // Схема пользователя    // натуральный ключ
    private String         name;         // Имя документа         // натуральный ключ
    private ZonedDateTime  date;         // Дата документа        // натуральный ключ
    private String          location;     // строка-описание источника документа (адрес источника)
    private String          text;         // текст документа
    private UUID         purpose;
    private String			mime;         // mime тип документа
    private Boolean         treated;		// Обработанный
}
