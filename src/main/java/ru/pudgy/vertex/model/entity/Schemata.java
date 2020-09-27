package ru.pudgy.vertex.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pudgy.vertex.utils.JsonIsoDateDeserializer;
import ru.pudgy.vertex.utils.JsonIsoDateSerializer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schemata {
    @Id
    private UUID            id;
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime   origin;
    private String 		    name;
    @JsonDeserialize(using = JsonIsoDateDeserializer.class)
    @JsonSerialize(using = JsonIsoDateSerializer.class)
    private ZonedDateTime   access;
    private boolean 	    enable;
}
