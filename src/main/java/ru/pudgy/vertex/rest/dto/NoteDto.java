package ru.pudgy.vertex.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class NoteDto {
    private UUID id;
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime origin;
    private String text;
    private String annotation;
    private UUID purpose;
    private String purposeName;
    private String purposeColor;
}
