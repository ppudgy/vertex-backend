package ru.pudgy.vertex.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class TodoNewDto {
    UUID				purpose;
    Boolean             external;           //Зависимость от внешних факторов (людей): зависит, независит
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    ZonedDateTime       relevance;          //Сроком актуальности
    String              description;
    Integer             auto;
    Integer             autoperiod;
}
