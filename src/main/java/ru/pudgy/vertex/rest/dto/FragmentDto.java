package ru.pudgy.vertex.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class FragmentDto {
    private UUID id;
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime origin;
    private String name;
    private UUID document;
    private String docname;
    private String annotation;
    private String text;
    private int start;
    private int end;
    private boolean roll;
}
