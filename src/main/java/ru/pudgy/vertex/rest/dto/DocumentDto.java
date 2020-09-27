package ru.pudgy.vertex.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DocumentDto {
    @Id
    private UUID id;           // Сурогатный ключ
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime origin;       // Дата и время внесения информации в банк данных
    private String         name;         // Имя документа         // натуральный ключ
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime  date;         // Дата документа        // натуральный ключ
    private String          location;     // строка-описание источника документа (адрес источника)
    private String          text;         // текст документа
    private String          annotation;
    private UUID            purpose;
    private String          purposeName;
    private String          purposeColor;
    private String			mime;          // mime тип документа
    private Boolean         treated;		// Обработанный
    private Boolean         roll;           // признак отображения полного текста в интерфейсе
}
