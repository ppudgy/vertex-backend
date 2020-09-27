package ru.pudgy.vertex.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class TodoDto {
    private UUID id;
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime origin;             // Время создания
    private UUID				purpose;
    private Boolean             external;           //Зависимость от внешних факторов (людей): зависит, независит
    private Boolean             done;               // признак окончания
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime       relevance;          //Сроком актуальности
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime       endtime;            // Окончание выполнения
    private String              description;
    private Integer             auto;
    private Integer             autoperiod;
    private String purposeName;
    private String purposeColor;

}
