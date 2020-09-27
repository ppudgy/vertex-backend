package ru.pudgy.vertex.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JsonIsoDateSerializer extends JsonSerializer<ZonedDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT; //!!! Important for format of 2019-09-02T16:28:39.280Z

    @Override
    public void serialize(ZonedDateTime date, JsonGenerator generator, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        final String dateString = date.format(this.formatter);
        generator.writeString(dateString);
    }
}